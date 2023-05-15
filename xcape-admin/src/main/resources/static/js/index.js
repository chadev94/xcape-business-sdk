// (function () {
let merchantId = document.getElementById('merchantId');
const themeId = document.getElementById('themeId');
const themeNameKo = document.getElementById('themeNameKo');
const themeNameEn = document.getElementById('themeNameEn');
const difficulty = document.getElementById('difficulty');
const description = document.getElementById('description');
const minParticipantCount = document.getElementById('minParticipantCount');
const maxParticipantCount = document.getElementById('maxParticipantCount');
const genre = document.getElementById('genre');
const point = document.getElementById('point');
const mainImagePreview = document.getElementById('mainImagePreview');
const bgImagePreview = document.getElementById('bgImagePreview');
const hasXKit = document.themeInfo.hasXKit;
const isCrimeScene = document.themeInfo.isCrimeScene;
const youtubeLink = document.getElementById('youtubeLink');
const colorCode = document.getElementById('colorCode');
const priceTemplate = document.querySelector('#priceTemplate').innerHTML;

const deletedPriceArr = [];
const deletedTimetableArr = [];

const getThemeInformation = (e) => {
    const id = e.currentTarget.dataset.themeId;
    axios.get(`/themes/${id}`).then((res) => {
        const {resultCode} = res.data;
        const theme = res.data.result;
        if (resultCode === SUCCESS) {
            document.themeInfo.action = `/themes/${theme.id}`;
            merchantId.value = theme.merchantId;
            themeId.value = theme.id;
            themeNameKo.value = theme.nameKo;
            themeNameEn.value = theme.nameEn;
            difficulty.value = theme.difficulty;
            description.value = theme.description;
            minParticipantCount.value = theme.minParticipantCount;
            maxParticipantCount.value = theme.maxParticipantCount;
            genre.value = theme.genre;
            point.value = theme.point;
            hasXKit.value = theme.hasXKit;
            isCrimeScene.value = theme.isCrimeScene;
            mainImagePreview.src = theme.mainImagePath || '/images/noPhoto.jpg';
            bgImagePreview.src = theme.bgImagePath || '/images/noPhoto.jpg';
            youtubeLink.value = theme.youtubeLink;
            colorCode.value = theme.colorCode || '#242424';
            bindAbility(theme.abilityList);
            // bindTimetableInputs(theme.timetable);
        }
    });
}

document.querySelectorAll('#treeArea .accordion-body button').forEach((button) => {
    button.addEventListener('click', getThemeInformation);
});

document.querySelector('#saveThemeButton').addEventListener('click', () => {
    const form = document.querySelector('.needs-validation');

    if (form.checkValidity()) {
        const formData = new FormData(form);
        const themeImageFormData = new FormData(document.themeImage);

        const abilityList = makeAbilityParameter();
        abilityList.forEach((ability, index) => {
            formData.append(`abilityList[${index}].id`, ability.id);
            formData.append(`abilityList[${index}].code`, ability.code);
            formData.append(`abilityList[${index}].name`, ability.name);
            formData.append(`abilityList[${index}].value`, ability.value);
        });

        // formData.append('timetable', makeTimetableParameter());

        if (document.themeImage.mainImage.value !== '') {
            formData.append('mainImage', themeImageFormData.get('mainImage'));
        }
        if (document.themeImage.bgImage.value !== '') {
            formData.append('bgImage', themeImageFormData.get('bgImage'));
        }

        const saveThemeButton = document.getElementById('saveThemeButton');
        const spinner = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                         <span>저장 중입니다...</span>`;
        saveThemeButton.disabled = true;
        saveThemeButton.innerHTML = spinner;

        axios.put(`/themes/${themeId.value}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then((res) => {
            if (res.data.resultCode === SUCCESS) {
                alert('성공적으로 저장했습니다. 👏');
            } else {
                alert('저장 중 에러가 발생했습니다. 😭')
            }
        }).finally(() => {
            saveThemeButton.disabled = false;
            saveThemeButton.innerHTML = '저장';
        });
    }

    form.classList.add('was-validated');
});

// 각 select 태그에 0 ~ 5 까지 만듦
let abilityOptions = '';
for (let i = 0; i <= 5; i++) {
    if (i === 3) {
        abilityOptions += `<option value="${i}" selected>${i}</option>`;
    } else {
        abilityOptions += `<option value="${i}">${i}</option>`;
    }
}

document.querySelector('#difficulty').innerHTML = abilityOptions;

document.querySelectorAll('.form-select.ability').forEach((select) => {
    select.innerHTML = abilityOptions;
});

let hourOptions = '<option value="00" selected>00</option>';
for (let i = 1; i <= 23; i++) {
    hourOptions += `<option value="${formattingTime(i)}">${formattingTime(i)}</option>`;
}

// document.querySelector('.form-select.hour').innerHTML = hourOptions;

let minuteOptions = '<option value="00" selected>00</option>';
for (let i = 0; i < 12; i++) {
    let everyFiveMinutes = i * 5;
    minuteOptions += `<option value="${formattingTime(everyFiveMinutes)}">${formattingTime(everyFiveMinutes)}</option>`
}

// document.querySelector('.form-select.minute').innerHTML = minuteOptions;

// 테마 클릭 이벤트
const addClickEventToAccordion = () => {
    const listGroup = document.querySelectorAll('.list-group button');
    listGroup.forEach((list) => {
        list.addEventListener('click', () => {
            clearValidity();
            clearDeletedPriceArr();
            clearImageInputs();
            document.querySelector('.list-group .active')?.classList.remove('active');
            list.classList.add('active');
        });
    });
}

document.querySelector('#addPriceButton').addEventListener('click', () => {
    createPriceInputs();
});

const createPriceInputs = () => {
    const priceDomCount = document.querySelectorAll('.person').length;
    const priceAreaId = `priceAreaId-${priceDomCount}`;
    const priceId = '';
    const personValue = '1';
    const priceValue = '10000';

    const priceInput = interpolate(priceTemplate, {priceAreaId, priceId, personValue, priceValue});
    const priceArea = document.querySelector(`#priceArea`);

    priceArea.insertAdjacentHTML('beforeend', priceInput);

    document.querySelector(`#${priceAreaId} .price-input`).addEventListener('change', formattingNumber);
    document.querySelector(`#${priceAreaId} .price-input`).addEventListener('input', onlyNumber);
    document.querySelector(`#${priceAreaId} .delete-price-button`).addEventListener('click', () => deletePrice(`${priceAreaId}`));
}

const deletePrice = (priceAreaId) => {
    const priceDom = document.querySelector(`#${priceAreaId}`);
    if (priceDom.dataset.priceId) {
        deletedPriceArr.push({
            id: priceDom.dataset.priceId,
            person: document.querySelector(`#${priceAreaId} .person`).value,
            price: document.querySelector(`#${priceAreaId} .price`).value.replace(/,/g, ""),
            themeId: themeId.value,

        });
    }
    priceDom.remove();
}

const bindPriceInputs = (priceList) => {
    let priceInputs = '';
    const priceTemplate = document.querySelector('#priceTemplate').innerHTML;
    document.querySelector('#priceArea').innerHTML = '';

    priceList.forEach((priceItem) => {
        const {id, person, price} = priceItem;
        const priceAreaId = `priceArea-${id}`;
        const priceId = id;
        const personValue = person;
        const priceValue = formattingNumber(price);
        priceInputs += interpolate(priceTemplate, {priceAreaId, priceId, personValue, priceValue});
    });

    document.querySelector(`#priceArea`).innerHTML = priceInputs;

    document.querySelectorAll('.price-input').forEach((input) => {
        input.addEventListener('change', formattingNumber);
    });

    document.querySelectorAll('.price-input').forEach((input) => {
        input.addEventListener('input', onlyNumber);
    });

    document.querySelectorAll('.delete-price-button').forEach((deleteButton) => {
        const {priceAreaId} = deleteButton.dataset;
        deleteButton.addEventListener('click', () => deletePrice(`${priceAreaId}`))
    });
}

const bindPriceDetail = () => {
    deletedPriceArr.length = 0;

    axios.get(`/themes/${themeId.value}/price`).then(res => {
        const {resultCode, result} = res.data;
        if (resultCode === SUCCESS) {
            bindPriceInputs(result);
        }
    });
};

const savePrice = () => {
    const params = makePriceParameter();

    if (params.length > 0) {
        axios.put(`/themes/${themeId.value}/price`, params).then(res => {
            const {resultCode} = res.data;
            if (resultCode === SUCCESS) {
                const priceDetailModal = document.querySelector('#priceDetailModal');
                const modal = bootstrap.Modal.getInstance(priceDetailModal);
                modal.hide();
                alert('성공적으로 저장했습니다.');
            } else {
                alert('저장에 실패했습니다.');
            }
        });
    } else {
        alert('저장할 가격이 없습니다.')
    }
}

const createTimetableInputs = () => {
    const timetableChildElementCount = document.querySelector('#timetableArea').childElementCount;
    let timetableCount = 1;
    if (timetableChildElementCount > 0) {
        const timetableChildElements = document.querySelectorAll('#timetableArea > div');
        timetableCount = timetableChildElements[timetableChildElements.length - 1].id.split('-')[1];
        timetableCount++;
    }
    const timetableAreaId = `timetableArea-${timetableCount}`;
    const timetableId = '';
    const hourId = `hour-${timetableCount}`;
    const minuteId = `minute-${timetableCount}`;
    const priceTemplate = document.querySelector('#timetableTemplate').innerHTML;
    const timetableInput = interpolate(priceTemplate, {timetableAreaId, timetableId, hourId, minuteId});
    document.querySelector(`#timetableArea`).insertAdjacentHTML('beforeend', timetableInput);
}

document.querySelector('#addTimetableButton').addEventListener('click', createTimetableInputs);

document.querySelector('#priceDetailButton').addEventListener('click', bindPriceDetail);

document.querySelector('#priceSaveButton').addEventListener('click', savePrice);

const bindTimetableInputs = (timetableList) => {
    let timetableInputs = '';
    const timetableTemplate = document.querySelector('#timetableTemplate').innerHTML;

    timetableList.forEach((timetable) => {
        const {id, time} = timetable;
        const timetableAreaId = `timetableArea-${id}`;
        const timetableId = id;
        const hour = time.split(':')[0];
        const hourId = `hour-${id}`;
        const minute = time.split(':')[1];
        const minuteId = `minute-${id}`;
        timetableInputs += interpolate(timetableTemplate, {timetableAreaId, timetableId, hour, hourId, minute, minuteId});
    });

    document.querySelector(`#timetableArea`).innerHTML = timetableInputs;

    timetableList.forEach((timetable) => {
        const {id} = timetable;
        const hour = timetable.time.split(':')[0];
        const minute = timetable.time.split(':')[1];
        document.querySelector(`#hour-${id}`).value = hour;
        document.querySelector(`#minute-${id}`).value = minute;
    });
}

const bindTimetableDetail = () => {
    deletedTimetableArr.length = 0;

    axios.get(`/themes/${themeId.value}/timetable`).then(res => {
        const {resultCode, result} = res.data;
        if (resultCode === SUCCESS) {
            result.sort((a, b) => {
                return a.time.localeCompare(b.time);
            });
            bindTimetableInputs(result);
        }
    });
}

document.querySelector('#timetableDetailButton').addEventListener('click', bindTimetableDetail);

const deleteTimetable = (timetableId) => {
    const timetableDom = document.querySelector(`#${timetableId}`);
    if (timetableDom.dataset.timetableId) {
        const hour = document.querySelector(`#${timetableId} .hour`).value;
        const minute = document.querySelector(`#${timetableId} .minute`).value
        deletedTimetableArr.push({
            id: timetableDom.dataset.timetableId,
            time: `${hour}:${minute}`,
        });
    }
    timetableDom.remove();
}

const clearValidity = () => {
    document.themeInfo.classList.remove('was-validated');
}

document.getElementById('youtubeLink').addEventListener('change', () => {
    const youtubeArea = document.getElementById('youtubeArea');
    let urlParams;
    try {
        urlParams = new URL(youtubeLink.value).searchParams;
    } catch (e) {
        youtubeArea.innerHTML = '';
        return;
    }
    const youtubeTemplate = document.getElementById('youtube-template').innerHTML;
    youtubeArea.innerHTML = youtubeTemplate.replace('{viewKey}', urlParams.get('v'));
});

const selectFirstTheme = () => {
    const firstTheme = document.querySelector('.accordion .list-group-item');
    if (firstTheme) {
        firstTheme.classList.add('active');
        firstTheme.click();
    }
}

const clearDeletedPriceArr = () => {
    deletedPriceArr.length = 0;
}

const clearImageInputs = () => {
    const mainImageInput = document.querySelector('#mainImage');
    const mainImagePreview = document.querySelector('#mainImagePreview');
    const bgImageInput = document.querySelector('#bgImage');
    const bgImagePreview = document.querySelector('#bgImagePreview');

    mainImageInput.value = '';
    mainImagePreview.src = '/images/noPhoto.jpg';
    bgImageInput.value = '';
    bgImagePreview.src = '/images/noPhoto.jpg';
}

const displayedPriceToParameter = () => {
    const priceParamArr = [];
    const priceArea = document.querySelector('#priceArea');
    const priceList = document.querySelectorAll('#priceArea .price');
    const personList = document.querySelectorAll('#priceArea .person');

    for (let i = 0; i < priceArea.childElementCount; i++) {
        priceParamArr.push({
            id: priceArea.children[i].dataset.priceId,
            person: personList[i].value,
            price: priceList[i].value.replace(/,/g, ""),
            isUsed: true
        });
    }

    return priceParamArr;
}

const makePriceParameter = () => {
    const priceParamArr = displayedPriceToParameter();

    deletedPriceArr.forEach(price => {
        priceParamArr.push({
            id: price.id,
            person: price.person,
            price: price.price,
            isUsed: false
        });
    });

    return priceParamArr;
}

const bindAbility = (abilityList) => {
    const abilityTemplate = document.querySelector('#abilityTemplate').innerHTML;
    const abilityArea = document.querySelector('#abilityArea');
    abilityArea.innerHTML = '';
    abilityList.forEach((ability) => {
        const abilityDom = abilityTemplate
            .replaceAll('{abilityCode}', ability.code)
            .replaceAll('{abilityName}', ability.name)
            .replace('{abilityId}', ability.id);

        abilityArea.insertAdjacentHTML('beforeend', abilityDom);
        document.querySelector(`#${ability.code}`).value = ability.value;
    });
}

const makeAbilityParameter = () => {
    const abilityParameter = [];
    document.querySelectorAll('.ability').forEach((ability) => {
        abilityParameter.push({
            id: ability.dataset.abilityId,
            code: ability.id,
            name: ability.dataset.abilityName,
            value: ability.value
        });
    });
    return abilityParameter;
}

const displayedTimetableToParameter = () => {
    const timetableParamArr = [];
    const timetableArea = document.querySelector('#timetableArea');
    const hourList = document.querySelectorAll('#timetableArea .hour');
    const minuteList = document.querySelectorAll('#timetableArea .minute');

    for (let i = 0; i < timetableArea.childElementCount; i++) {
        const hour = hourList[i].value;
        const minute = minuteList[i].value;
        const time = `${hour}:${minute}`;
        timetableParamArr.push({
            id: timetableArea.children[i].dataset.timetableId,
            time: time,
            isUsed: true
        });
    }

    return timetableParamArr;
}

const makeTimetableParameter = () => {
    const timetableParamArr = displayedTimetableToParameter();

    deletedTimetableArr.forEach(timetable => {
        timetableParamArr.push({
            id: timetable.id,
            time: timetable.time,
            isUsed: false
        });
    });

    return timetableParamArr;
}

document.querySelector('#timetableSaveButton').addEventListener('click', () => {
    const params = makeTimetableParameter();

    if (params.length > 0) {
        axios.put(`/themes/${themeId.value}/timetable`, params).then(res => {
            const {resultCode} = res.data;
            if (resultCode === SUCCESS) {
                const timetableModal = document.querySelector('#timetableDetailModal');
                const modal = bootstrap.Modal.getInstance(timetableModal);
                modal.hide();
                alert('성공적으로 저장했습니다.');
            } else {
                alert('저장에 실패했습니다');
            }
        });
    } else {
        alert('저장할 타임테이블이 없습니다.')
    }
});

const init = () => {
    addClickEventToAccordion();
    selectFirstTheme();
}

init();

// })();

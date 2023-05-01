// (function () {
const merchantId = document.getElementById('merchantId');
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

        formData.append('timetable', makeTimetableParameter());

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

document.querySelector('.form-select.hour').innerHTML = hourOptions;

let minuteOptions = '<option value="00" selected>00</option>';
for (let i = 0; i < 12; i++) {
    let everyFiveMinutes = i * 5;
    minuteOptions += `<option value="${formattingTime(everyFiveMinutes)}">${formattingTime(everyFiveMinutes)}</option>`
}

document.querySelector('.form-select.minute').innerHTML = minuteOptions;

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
    const priceAreaId = `price-${priceDomCount}`;
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
        const priceAreaId = `price-${priceItem.id}`;
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
        bindPriceInputs(res.data.result);
    });
};

const savePrice = () => {
    const params = makePriceParameter();

    axios.put(`/themes/${themeId.value}/price`, params).then(res => {
        const {resultCode} = res.data;
        if (resultCode === SUCCESS) {
            return axios.delete(`/themes/${themeId.value}/price`, {data: deletedPriceArr});
        }
    }).then((res) => {
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
    const hourId = `hour-${timetableCount}`;
    const minuteId = `minute-${timetableCount}`;
    const priceTemplate = document.querySelector('#timetableTemplate').innerHTML;
    const timetableInput = interpolate(priceTemplate, {timetableAreaId, hourId, minuteId});
    document.querySelector(`#timetableArea`).insertAdjacentHTML('beforeend', timetableInput);
}

document.querySelector('#addTimetableButton').addEventListener('click', createTimetableInputs);

document.querySelector('#priceDetailButton').addEventListener('click', bindPriceDetail);

document.querySelector('#priceSaveButton').addEventListener('click', savePrice);

const bindTimetableInputs = (timetableInfo) => {
    let timetableInputs = '';
    const timetableTemplate = document.querySelector('#timetableTemplate').innerHTML;
    if (timetableInfo) {
        const timetableArray = timetableInfo.split(',');
        let hour = [];
        let minute = [];
        timetableArray.forEach((item, index) => {
            const id = index + 1;
            hour.push(item.split(':')[0]);
            minute.push(item.split(':')[1]);

            const timetableAreaId = `timetableArea-${id}`;
            const hourId = `hour-${id}`;
            const minuteId = `minute-${id}`;

            timetableInputs += interpolate(timetableTemplate, {timetableAreaId, hourId, minuteId});
        });

        document.querySelector(`#timetableArea`).innerHTML = timetableInputs;

        timetableArray.forEach((timetable, index) => {
            document.getElementById(`hour-${index + 1}`).value = hour[index];
            document.getElementById(`minute-${index + 1}`).value = minute[index];
        });
    } else {
        const timetableAreaId = `timetableArea-1`;
        const hourId = 'hour-1';
        const minuteId = 'minute-1';

        timetableInputs = interpolate(timetableTemplate, {timetableAreaId, hourId, minuteId});
        document.getElementById(`timetableArea`).innerHTML = timetableInputs;
    }
}

const bindTimetableDetail = () => {
    axios.get(`/themes/${themeId.value}/timetable`).then(res => {
        if (res.resultCode === SUCCESS) {
            res.result.sort((a, b) => {
                return b.time - a.time;
            });
        }
    });
}

document.querySelector('#timetableDetailButton').addEventListener('click', () => bindTimetableDetail);

const deleteTimetable = (timetableId) => {
    const id = `${timetableId.split('-')[0]}`;
    const timetableArea = document.getElementById(id);
    if (timetableArea.childElementCount < 2) {
        alert('예약가능 시간 정보는 1개 이상이어야 합니다.');
    } else {
        document.getElementById(timetableId).remove();
    }
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

/**
 * @description 화면 init 시 첫번째 테마를 선택하는 함수. 항상 페이지 맨 밑에 있어야 함.
 */
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

const displayedPriceToParameter = (paramArr) => {
    const priceArea = document.querySelector('#priceArea');
    const priceList = document.querySelectorAll('#priceArea .price');
    const personList = document.querySelectorAll('#priceArea .person');

    for (let i = 0; i < priceArea.childElementCount; i++) {
        paramArr.push({
            id: priceArea.children[i].dataset.priceId,
            person: personList[i].value,
            price: priceList[i].value.replace(/,/g, ""),
            themeId: themeId.value,
        });
    }
}

const makePriceParameter = () => {
    const priceParamArr = [];

    displayedPriceToParameter(priceParamArr);
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

const makeTimetableParameter = () => {
    const timetableArray = [];
    const timetableChildElements = document.querySelectorAll('#timetableArea > div');
    if (timetableChildElements.length > 0) {
        timetableChildElements.forEach((element, index) => {
            const id = element.id.split('-')[1];
            const hour = document.querySelector(`#hour-${id}`).value;
            const minute = document.querySelector(`#minute-${id}`).value;
            timetableArray[index] = `${hour}:${minute}`;
        });
        timetableArray.sort();
    }
    return timetableArray.join(',');
}

const init = () => {
    addClickEventToAccordion();
    selectFirstTheme();
}

init();

// })();

const merchantId = document.getElementById('merchantId');
const themeId = document.getElementById('themeId');
const themeNameKo = document.getElementById('themeNameKo');
const themeNameEn = document.getElementById('themeNameEn');
const difficulty = document.getElementById('difficulty');
const description = document.getElementById('description');
const reasoning = document.getElementById('reasoning');
const observation = document.getElementById('observation');
const activity = document.getElementById('activity');
const teamwork = document.getElementById('teamwork');
const minParticipantCount = document.getElementById('minParticipantCount');
const maxParticipantCount = document.getElementById('maxParticipantCount');
const genre = document.getElementById('genre');
const point = document.getElementById('point');
const mainImagePreview = document.getElementById('mainImagePreview');
const bgImagePreview = document.getElementById('bgImagePreview');
const hasXKit = document.themeInfo.hasXKit;
const isCrimeScene = document.themeInfo.isCrimeScene;
const youtubeLink = document.getElementById('youtubeLink');

const deletedPriceArr = [];

const refreshAccordionList = () => {
    axios.get('/merchants').then((res) => {
        const {resultCode} = res.data;
        const merchantList = res.data.result;
        if (resultCode === SUCCESS) {
            let accordionHeader = '';
            merchantList.map((merchant) => {
                accordionHeader += `<div class="accordion-item">
                                       <h2 class="accordion-header" id="merchant-${merchant.id}">
                                           <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                           data-bs-target="#themeList-${merchant.id}" aria-expanded="false">${merchant.name}</button>
                                       </h2>
                                       <div id="themeList-${merchant.id}" class="accordion-collapse collapse"
                                           aria-labelledby="merchant-${merchant.id}">
                                               <div class="accordion-body">
                                                   <div class="list-group list-group-flush">`;
                merchant.themeList.forEach((theme) => {
                    accordionHeader += `<button type="button" class="list-group-item list-group-item-action"
                                           onclick="getThemeInformation(${theme.id})">${theme.nameKo}</button>`;
                });
               accordionHeader += '</div></div></div></div>';
            });
            const accordionArea = document.querySelector('#treeArea .accordion');
            accordionArea.innerHTML = accordionHeader;
            if (accordionArea.childElementCount > 0) {
                clearValidity();
                addClickEventToAccordion();
                selectFirstTheme();
                document.querySelector('.accordion-item button').attributes['aria-expanded'].value = true
                document.querySelector('.accordion-item button').classList.remove('collapsed');
                document.querySelector('#treeArea .accordion-item div').classList.add('show')
            }
        }
    });
}

const getThemeInformation = (id) => {
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
            reasoning.value = theme.reasoning;
            observation.value = theme.observation;
            activity.value = theme.activity;
            teamwork.value = theme.teamwork;
            minParticipantCount.value = theme.minParticipantCount;
            maxParticipantCount.value = theme.maxParticipantCount;
            genre.value = theme.genre;
            point.value = theme.point;
            hasXKit.value = theme.hasXKit;
            isCrimeScene.value = theme.isCrimeScene;
            mainImagePreview.src = theme.mainImagePath || '/images/noPhoto.jpg';
            bgImagePreview.src = theme.bgImagePath || '/images/noPhoto.jpg';
            youtubeLink.value = theme.youtubeLink;
            bindingTimetableInputs(theme.timetable);
            getPriceList(id);
        }
    });
}

const getPriceList = (themeId) => {
    axios.get('/price', {params: {themeId}}).then((res) => {
        const {resultCode} = res.data;
        if (resultCode) {
            const priceList = res.data.result;
            const generalPriceList = priceList.filter((price) => {
                return price.type === GENERAL;
            });
            const openRoomPriceList = priceList.filter((price) => {
                return price.type === OPEN_ROOM;
            });

            generalPriceList.sort((a, b) => {
                return a.person - b.person;
            });
            openRoomPriceList.sort((a, b) => {
                return a.person - b.person;
            });

            bindPriceInputs(GENERAL_PRICE_AREA, generalPriceList);
            bindPriceInputs(OPEN_ROOM_PRICE_AREA, openRoomPriceList);
        }
    });
}

document.getElementById('saveThemeButton').addEventListener('click', () => {
    const form = document.querySelector('.needs-validation');

    if (form.checkValidity()) {
        const formData = new FormData(form);
        const themeImageFormData = new FormData(document.themeImage);

        const param = {
            merchantId: formData.get('merchantId'),
            id: formData.get('themeId'),
            nameKo: formData.get('themeNameKo'),
            nameEn: formData.get('themeNameEn'),
            difficulty: formData.get('difficulty'),
            timetable: sortTimetable().toString(),
            description: formData.get('description'),
            reasoning: formData.get('reasoning'),
            observation: formData.get('observation'),
            activity: formData.get('activity'),
            teamwork: formData.get('teamwork'),
            minParticipantCount: formData.get('minParticipantCount'),
            maxParticipantCount: formData.get('maxParticipantCount'),
            genre: formData.get('genre'),
            colorCode: formData.get('colorCode'),
            point: formData.get('point'),
            youtubeLink: formData.get('youtubeLink'),
            hasXKit: formData.get('hasXKit'),
            isCrimeScene: formData.get('isCrimeScene')
        };

        if (document.themeImage.mainImage.value !== '') {
            param.mainImage = themeImageFormData.get('mainImage');
        }
        if (document.themeImage.bgImage.value !== '') {
            param.bgImage = themeImageFormData.get('bgImage')
        }

        const saveThemeButton = document.getElementById('saveThemeButton');
        const spinner = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                         <span>저장 중입니다...</span>`;
        saveThemeButton.disabled = true;
        saveThemeButton.innerHTML = spinner;

        axios.all([
            axios.put(`/themes/${themeId.value}`, param, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }),
            axios.post('/price', makePriceParameter(), {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
        ]).then(
            axios.spread((modifyThemeDetailResult, savePriceListResult) => {
                if (modifyThemeDetailResult.data.resultCode === SUCCESS
                    && savePriceListResult.data.resultCode === SUCCESS) {
                    alert('성공적으로 저장했습니다. 👏');
                } else {
                    alert('저장 중 에러가 발생했습니다. 😭')
                }
            })
        ).finally(() => {
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

document.getElementById('difficulty').innerHTML = abilityOptions;

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

document.getElementById('addGeneralPriceButton').addEventListener('click', () => {
    createPriceInputs(GENERAL_PRICE_AREA);
});

document.getElementById('addOpenRoomPriceButton').addEventListener('click', () => {
    createPriceInputs(OPEN_ROOM_PRICE_AREA);
});

const createPriceInputs = (areaType) => {
    const person = document.querySelectorAll(`input[name="person"]`);
    const personCount = parseInt(person[person.length - 1].value.replace(/,/g, '')) + 1 || 1;
    const priceTemplate = document.getElementById('priceTemplate').innerHTML;
    const priceInput = priceTemplate.replaceAll('{priceAreaId}', `${areaType}-${personCount}`)
        .replace('{priceId}', null)
        .replace('{personValue}', personCount.toString())
        .replace('{priceValue}', '0');
    document.getElementById(`${areaType}`).insertAdjacentHTML('beforeend', priceInput);
}

const deletePrice = (priceAreaId) => {
    const id = `${priceAreaId.split('-')[0]}`;
    const priceArea = document.getElementById(id);
    if (priceArea.childElementCount < 2) {
        alert('가격 정보는 1개 이상이어야 합니다.');
    } else {
        const priceDom = document.getElementById(priceAreaId);
        deletedPriceArr.push({
            id: priceDom.dataset.priceId,
            person: document.querySelector(`#${priceAreaId} input[name="person"]`).value,
            price: document.querySelector(`#${priceAreaId} input[name="price"]`).value.replace(/,/g, ""),
            type: id.replace('Area', ''),
            themeId: themeId.value,
            useYn: false
        });
        priceDom.remove();
    }
}

const bindPriceInputs = (areaType, priceArray) => {
    let priceInputs = '';
    const priceTemplate = document.getElementById('priceTemplate').innerHTML;
    if (priceArray.length > 0) {
        priceArray.forEach((item) => {
            priceInputs += priceTemplate.replaceAll('{priceAreaId}', `${areaType}-${item.person}`)
                .replace('{priceId}', item.id)
                .replace('{personValue}', item.person)
                .replace('{priceValue}', formattingNumber(item.price));
        });
    } else {
        priceInputs = priceTemplate.replaceAll('{priceAreaId}', `${areaType}-1`)
            .replace('{priceId}', null)
            .replace('{personValue}', '1')
            .replace('{priceValue}', '1000');
    }

    document.getElementById(`${areaType}`).innerHTML = priceInputs;
}

const imagePreview = (element) => {
    const [file] = element.files;
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            const imagePreview = document.getElementById(element.id + 'Preview');
            imagePreview.src = e.target.result;
        }
        reader.readAsDataURL(file);
    }
}

document.getElementById('addTimetableButton').addEventListener('click', () => {
    createTimetableInputs();
});

const createTimetableInputs = () => {
    const timetableChildElementCount = document.getElementById('timetableArea').childElementCount;
    let timetableCount = 1;
    if (timetableChildElementCount > 0) {
        const timetableChildElements = document.querySelectorAll('#timetableArea > div');
        timetableCount = timetableChildElements[timetableChildElements.length - 1].id.split('-')[1];
        timetableCount++;
    }
    const priceTemplate = document.getElementById('timetable-template').innerHTML;
    const timetableInput = priceTemplate.replaceAll('{timetableAreaId}', `timetableArea-${timetableCount}`)
        .replace('{hourId}', `hour-${timetableCount}`)
        .replace('{minuteId}', `minute-${timetableCount}`);
    document.getElementById(`timetableArea`).insertAdjacentHTML('beforeend', timetableInput);
}

const bindingTimetableInputs = (timetableInfo) => {
    let timetableInputs = '';
    const timetableTemplate = document.getElementById('timetable-template').innerHTML;
    if (timetableInfo) {
        const timetableArray = timetableInfo.split(',');
        let hour = [];
        let minute = [];
        timetableArray.forEach((item, index) => {
            const id = index + 1;
            hour.push(item.split(':')[0]);
            minute.push(item.split(':')[1]);
            timetableInputs += timetableTemplate.replaceAll('{timetableAreaId}', `timetableArea-${id}`)
                .replace('{hourId}', `hour-${id}`)
                .replace('{minuteId}', `minute-${id}`);
        });

        document.getElementById(`timetableArea`).innerHTML = timetableInputs;

        for (let i = 0; i < timetableArray.length; i++) {
            document.getElementById(`hour-${i + 1}`).value = hour[i];
            document.getElementById(`minute-${i + 1}`).value = minute[i];
        }
    } else {
        timetableInputs = timetableTemplate.replaceAll('{timetableAreaId}', `timetableArea-1`)
            .replace('{hourId}', `hour-1`)
            .replace('{minuteId}', `minute-1`);
        document.getElementById(`timetableArea`).innerHTML = timetableInputs;
    }
}

const deleteTimetable = (timetableId) => {
    const id = `${timetableId.split('-')[0]}`;
    const timetableArea = document.getElementById(id);
    if (timetableArea.childElementCount < 2) {
        alert('예약능 가능 시간 정보는 1개 이상이어야 합니다.');
    } else {
        document.getElementById(timetableId).remove();
    }
}

const sortTimetable = () => {
    let timetableArray = [];
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
    return timetableArray;
}

document.querySelectorAll('.btn').forEach((button) => {
   if (button.id !== 'saveThemeButton') {
       button.addEventListener('click', () => {
           clearValidity();
       });
   }
});

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
    firstTheme?.classList.add('active');
    firstTheme?.onclick();
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
    const openRoomPriceArea = document.querySelector('#openRoomPriceArea');
    const openRoomPriceList = document.querySelectorAll('#openRoomPriceArea input[name="price"]');
    const openRoomPersonList = document.querySelectorAll('#openRoomPriceArea input[name="person"]');

    const generalPriceArea = document.querySelector('#generalPriceArea');
    const generalPriceList = document.querySelectorAll('#generalPriceArea input[name="price"]');
    const generalPersonList = document.querySelectorAll('#generalPriceArea input[name="person"]');

    for (let i = 0; i < openRoomPriceArea.childElementCount; i++) {
        paramArr.push({
            id: openRoomPriceArea.children[i].dataset.priceId,
            person: openRoomPersonList[i].value,
            price: openRoomPriceList[i].value.replace(/,/g, ""),
            type: 'openRoom',
            themeId: themeId.value,
            useYn: true
        });
    }

    for (let i = 0; i < generalPriceArea.childElementCount; i++) {
        paramArr.push({
            id: generalPriceArea.children[i].dataset.priceId,
            person: generalPersonList[i].value,
            price: generalPriceList[i].value.replace(/,/g, ""),
            type: 'general',
            themeId: themeId.value,
            useYn: true
        });
    }
}

const makePriceParameter = () => {
    const priceParamArr = [];

    displayedPriceToParameter(priceParamArr);
    deletedPriceArr.forEach((deletedPrice) => {
        priceParamArr.push(deletedPrice)
    });
    return priceParamArr;
}

const init = () => {
    addClickEventToAccordion();
    selectFirstTheme();
}

init();

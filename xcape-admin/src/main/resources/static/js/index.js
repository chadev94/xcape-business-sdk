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
const minPersonnel = document.getElementById('minPersonnel');
const maxPersonnel = document.getElementById('maxPersonnel');
const genre = document.getElementById('genre');
const point = document.getElementById('point');
const mainImagePreview = document.getElementById('mainImagePreview');
const bgImagePreview = document.getElementById('bgImagePreview');
const hasXKit = document.themeInfo.hasXKit;
const isCrimeScene = document.themeInfo.isCrimeScene;
const youtubeLink = document.getElementById('youtubeLink');

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
               merchant.themeDtoList.forEach((theme) => {
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
            difficulty.value = theme.difficulty || 3;
            description.value = theme.description;
            reasoning.value = theme.reasoning || 3;
            observation.value = theme.observation || 3;
            activity.value = theme.activity || 3;
            teamwork.value = theme.teamwork || 3;
            minPersonnel.value = theme.minPersonnel || 2;
            maxPersonnel.value = theme.maxPersonnel || 4;
            genre.value = theme.genre;
            point.value = theme.point;
            hasXKit.value = theme.hasXKit || true;
            isCrimeScene.value = theme.isCrimeScene || false;
            mainImagePreview.src = theme.mainImagePath || '/images/noPhoto.jpg';
            bgImagePreview.src = theme.bgImagePath || '/images/noPhoto.jpg';
            youtubeLink.value = theme.youtubeLink;
            bindingPriceInputs(GENERAL_PRICE_AREA, GENERAL_PERSON, GENERAL_PRICE, theme.generalPrice);
            bindingPriceInputs(OPEN_ROOM_PRICE_AREA, OPEN_ROOM_PERSON, OPEN_ROOM_PRICE, theme.openRoomPrice);
            bindingTimetableInputs(theme.timetable);
        }
    });
}

document.getElementById('saveThemeButton').addEventListener('click', () => {
    const form = document.querySelector('.needs-validation');

    if (form.checkValidity()) {
        const formData = new FormData(form);
        const themeImageFormData = new FormData(document.themeImage);

        let param = {
            merchantId: formData.get('merchantId'),
            id: formData.get('themeId'),
            nameKo: formData.get('themeNameKo'),
            nameEn: formData.get('themeNameEn'),
            difficulty: formData.get('difficulty'),
            generalPrice: JSON.stringify(makePriceParameter(GENERAL_PRICE_AREA, GENERAL_PERSON, GENERAL_PRICE)),
            openRoomPrice: JSON.stringify(makePriceParameter(OPEN_ROOM_PRICE_AREA, OPEN_ROOM_PERSON, OPEN_ROOM_PRICE)),
            timetable: JSON.stringify(sortTimetable()),
            description: formData.get('description'),
            reasoning: formData.get('reasoning'),
            observation: formData.get('observation'),
            activity: formData.get('activity'),
            teamwork: formData.get('teamwork'),
            minPersonnel: formData.get('minPersonnel'),
            maxPersonnel: formData.get('maxPersonnel'),
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
        saveThemeButton.innerHTML = spinner

        axios.put(`/themes/${themeId.value}`, param, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then((res) => {
            saveThemeButton.disabled = false;
            saveThemeButton.innerHTML = '저장';
            if (res.data.resultCode === SUCCESS) {
                alert('저장 되었습니다.');
                refreshAccordionList();
            } else {
                alert('실패했습니다.');
            }
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
            document.querySelector('.list-group .active')?.classList.remove('active');
            list.classList.add('active');
        });
    });
}

document.getElementById('addGeneralPriceButton').addEventListener('click', () => {
    createPriceInputs(GENERAL_PRICE_AREA, GENERAL_PERSON, GENERAL_PRICE);
});

document.getElementById('addOpenRoomPriceButton').addEventListener('click', () => {
    createPriceInputs(OPEN_ROOM_PRICE_AREA, OPEN_ROOM_PERSON, OPEN_ROOM_PRICE);
});

const createPriceInputs = (areaType, personType, priceType) => {
    const person = document.querySelectorAll(`input[name="${personType}"]`);
    const personCount = parseInt(person[person.length - 1].value.replace(/,/g, '')) + 1 || 1;
    const priceTemplate = document.getElementById('price-template').innerHTML;
    const priceInput = priceTemplate.replaceAll('{priceAreaId}', `${areaType}-${personCount}`)
        .replace('{personId}', `${priceType}-${personCount}`)
        .replace('{personValue}', personCount.toString())
        .replace('{personName}', personType)
        .replace('{priceName}', priceType)
        .replace('{priceId}', `${priceType}-${personCount}`)
        .replace('{priceValue}', '0');
    document.getElementById(`${areaType}`).insertAdjacentHTML('beforeend', priceInput);
}

const deletePrice = (priceAreaId) => {
    const id = `${priceAreaId.split('-')[0]}`;
    const priceArea = document.getElementById(id);
    if (priceArea.childElementCount < 2) {
        alert('가격 정보는 1개 이상이어야 합니다.');
    } else {
        document.getElementById(priceAreaId).remove();
    }
}

const bindingPriceInputs = (areaType, personType, priceType, priceInfo) => {
    let priceInputs = '';
    const priceTemplate = document.getElementById('price-template').innerHTML;
    if (priceInfo) {
        const priceArray = JSON.parse(priceInfo);
        priceArray.forEach((item) => {
            priceInputs += priceTemplate.replaceAll('{priceAreaId}', `${areaType}-${item.person}`)
                                        .replace('{personId}', `${priceType}-${item.person}`)
                                        .replace('{personValue}', item.person)
                                        .replace('{personName}', personType)
                                        .replace('{priceName}', priceType)
                                        .replace('{priceId}', `${priceType}-${item.person}`)
                                        .replace('{priceValue}', formattingNumber(item.price));
        });
    } else {
        priceInputs = priceTemplate.replaceAll('{priceAreaId}', `${areaType}-0`)
            .replace('{personId}', `${priceType}-0`)
            .replace('{personValue}', '0')
            .replace('{personName}', personType)
            .replace('{priceName}', priceType)
            .replace('{priceId}', `${priceType}-0`)
            .replace('{priceValue}', '0');
    }

    document.getElementById(`${areaType}`).innerHTML = priceInputs;
}

const imagePreview = (element) => {
    const [file] = element.files;
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            const imagePreview = document.getElementById(element.id + 'Preview');
            console.log(e);
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
        const timetableArray = JSON.parse(timetableInfo);
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

const makePriceParameter = (priceArea, person, price) => {
    let priceArray = [];
    for (let i = 0; i < document.getElementById(priceArea).childElementCount; i++) {
        priceArray[i] = {
            person: document.getElementsByName(person)[i].value,
            price: document.getElementsByName(price)[i].value.replace(/,/g, '')
        };
    }
    return priceArray.sort((a, b) => {
        return a.person - b.person;
    });
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

const init = () => {
    addClickEventToAccordion();
    selectFirstTheme();
}

init();

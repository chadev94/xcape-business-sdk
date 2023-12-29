let merchantId = document.getElementById('merchantId');

const deletedPriceArr = [];
const deletedTimetableArr = [];

const getThemeInformation = (e) => {
    const id = e.currentTarget.dataset.themeId;
    axios.get(`/themes/${id}`).then((res) => {
        const {resultCode} = res.data;
        const theme = res.data.result;
        if (resultCode === SUCCESS) {
            document.themeInfo.action = `/themes/${theme.id}`;
            document.querySelector('#merchantId').value = theme.merchantId;
            document.querySelector('#themeId').value = theme.id;
            document.querySelector('#themeNameKo').value = theme.nameKo;
            document.querySelector('#themeNameEn').value = theme.nameEn;
            document.querySelector('#difficulty').value = theme.difficulty;
            document.querySelector('#description').value = theme.description;
            document.querySelector('#minParticipantCount').value = theme.minParticipantCount;
            document.querySelector('#maxParticipantCount').value = theme.maxParticipantCount;
            document.querySelector('#genre').value = theme.genre;
            document.querySelector('#point').value = theme.point;
            document.querySelector('input[name="hasXKit"]').value = theme.hasXKit;
            document.querySelector('input[name="isCrimeScene"]').value = theme.isCrimeScene;
            document.querySelector('#mainImagePreview').src = theme.mainImagePath || '/images/noPhoto.jpg';
            document.querySelector('#bgImagePreview').src = theme.bgImagePath || '/images/noPhoto.jpg';
            document.querySelector('#youtubeLink').value = theme.youtubeLink;
            document.querySelector('#colorCode').value = theme.colorCode || '#242424';
            document.querySelector('#runningTime').value = theme.runningTime;
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
        const themeId = document.querySelector('#themeId').value

        axios.put(`/themes/${themeId}`, formData, {
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
    const priceTemplate = document.querySelector('#priceTemplate').innerHTML;
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
            themeId: document.querySelector('#themeId').value,

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
        deleteButton.addEventListener('click', () => deletePrice(priceAreaId));
    });
}

const bindPriceDetail = () => {
    deletedPriceArr.length = 0;
    const themeId = document.querySelector('#themeId').value;

    axios.get(`/themes/${themeId}/price`).then(res => {
        const {resultCode, result} = res.data;
        if (resultCode === SUCCESS) {
            bindPriceInputs(result);
        }
    });
};

const savePrice = () => {
    const params = makePriceParameter();
    const themeId = document.querySelector('#themeId').value;

    if (params.length > 0) {
        axios.put(`/themes/${themeId}/price`, params).then(res => {
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
    const themeId = document.querySelector('#themeId').value;

    axios.get(`/themes/${themeId}/timetable`).then(res => {
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
        const youtubeLink = document.querySelector('#youtubeLink').value;
        urlParams = new URL(youtubeLink).searchParams;
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
        const themeId = document.querySelector('#themeId').value
        axios.put(`/themes/${themeId}/timetable`, params).then(res => {
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

// TODO: test용
const RELEASE_FILE_NAME = 'release.json'

const submit = () => {
    const form = new FormData();
    form.append('file', new File([JSON.stringify(release)], 'release.json'));
    form.append('type', 'theme');
    axios.putForm('/json', form).then(res => {console.log(res)});
};
const release = [{"ifffasdfasdfasdfff":1,"merchantId":1,"nameKo":"제3표류도","nameEn":"The Third-Drift Island","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/1/22d1492e-0da1-47b9-90b9-12464858e3f4_%E1%84%8C%E1%85%A63%E1%84%91%E1%85%AD%E1%84%85%E1%85%B2%E1%84%83%E1%85%A9.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/1/b5d4bafe-58ab-4e8f-b18e-57adfdd4cf9a_%E1%84%8C%E1%85%A63%E1%84%91%E1%85%AD%E1%84%85%E1%85%B2%E1%84%83%E1%85%A9%28%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5%29.png","timetable":null,"description":"지도에도 표시되지 않은 외딴곳에\r\n홀로 있는 의문의 수감시설.\r\n이곳에서 한 사건이 익명으로 신고되었다.\r\n이 시설은 왜 존재하고 여기선 무슨 일이 벌어지고 있는 것인가?","minParticipantCount":2,"maxParticipantCount":6,"difficulty":4,"genre":"수사추리/어드벤처","point":"의문의 사건속 실체를 파악하라..!","youtubeLink":"https://www.youtube.com/embed/VWHpBdqvR3M","colorCode":"#00ff40","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":2,"merchantId":1,"nameKo":"기억의 틈","nameEn":"Gap in Memory","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/2/7f6a332a-aa88-4def-a607-7c7d4f18aa36_%E1%84%80%E1%85%B5%E1%84%8B%E1%85%A5%E1%86%A8%E1%84%8B%E1%85%B4%E1%84%90%E1%85%B3%E1%86%B7%28%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5%29.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/2/2e6a9d65-bc14-4cc7-aa20-3e586008f819_%E1%84%80%E1%85%B5%E1%84%8B%E1%85%A5%E1%86%A8%E1%84%8B%E1%85%B4%E1%84%90%E1%85%B3%E1%86%B7.jpeg","timetable":null,"description":"잃어버린 그날의 기억을\r\n찾아주세요\r\n17년 전 10살의 그날로 돌아간다..\r\n\r\n아버지\r\n\r\n왜\r\n\r\n도대체\r\n\r\n그날, 나를 왜 버리고 떠났나요?","minParticipantCount":2,"maxParticipantCount":6,"difficulty":3,"genre":"추리/드라마","point":"최면수사를 통해 미제사건을 해결해 나간다","youtubeLink":"https://www.youtube.com/embed/8G70aalpZyg","colorCode":"#d86bff","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":3,"merchantId":1,"nameKo":"수취인초대","nameEn":"Receiver Invitation","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/3/e9166f38-7f50-4468-b4b9-c5ff5d7c4faf_%E1%84%89%E1%85%AE%E1%84%8E%E1%85%B1%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%8E%E1%85%A9%E1%84%83%E1%85%A2.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/3/6e536712-2a88-4e59-a56f-13ec4f9dbcf3_%E1%84%89%E1%85%AE%E1%84%8E%E1%85%B1%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%8E%E1%85%A9%E1%84%83%E1%85%A2%28%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5%29.jpeg","timetable":null,"description":"의문의 초대장이 도착했습니다.\r\n\r\n1일이 지나면 가까운 친구를 잃고\r\n2일이 지나면 당신에게 불행한 일이 생기고\r\n초대받은 지 3일째인 오늘\r\n\r\n오늘도 응하지 않으면 당신의 내일이 없을 겁니다.\r\n\r\n마지막 초대에 응해주세요.","minParticipantCount":2,"maxParticipantCount":6,"difficulty":4,"genre":"미스테리/스릴러","point":"'의문의 초대' 사건을 의뢰받은 엑스파일러","youtubeLink":"https://www.youtube.com/embed/-_uY62ISx1w","colorCode":"#FD5916","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":4,"merchantId":1,"nameKo":"제5수감동","nameEn":"No.5-PRISON","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/4/9cdf79e4-f696-40ea-8b54-4af98d93d05b_%E1%84%8C%E1%85%A65%E1%84%89%E1%85%AE%E1%84%80%E1%85%A1%E1%86%B7%E1%84%83%E1%85%A9%E1%86%BC.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/4/3fd2c263-d518-4e10-8ed8-c54c260645e1_%E1%84%8C%E1%85%A65%E1%84%89%E1%85%AE%E1%84%80%E1%85%A1%E1%86%B7%E1%84%83%E1%85%A9%E1%86%BC%28%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5%29.png","timetable":null,"description":"1987년 8월 14일, 남일동의 한 수감동.\r\n\r\n정부의 신임을 받는 특수한 시설의 수감동.\r\n알 수 없는 몰골의 참담한 모습으로\r\n발견된 수감동의 피해자.\r\n몸에는 무수한 자상히 남겨져 있다.\r\n\r\n이 비밀스러운 수감동에서\r\n치밀한 살인을 계획한 범인은 누구인가?\r\n죽음에 이르게 된 정황을 밝히기 위해\r\n4명의 유력 용의자를 소환한다!","minParticipantCount":4,"maxParticipantCount":6,"difficulty":2,"genre":"역사추리/드라마","point":"5인이상참여시 용의자외에탐정역할이추가됩니다. 역할은랜덤뽑기또는임의로선택할수있습니다.","youtubeLink":"https://www.youtube.com/embed/ESj-P4PRyWs","colorCode":"#fff200","hasXKit":false,"isCrimeScene":true,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":6,"merchantId":2,"nameKo":"숨바꼭질","nameEn":"Gap in Memory","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/6/d4ef1796-8624-42b5-9d9e-3e54dbc44a19_%E1%84%89%E1%85%AE%E1%86%B7%E1%84%87%E1%85%A1%E1%84%81%E1%85%A9%E1%86%A8%E1%84%8C%E1%85%B5%E1%86%AF%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/6/52c313ea-ede2-4d0f-9cd2-24886f105b7e_%E1%84%89%E1%85%AE%E1%86%B7%E1%84%87%E1%85%A1%E1%84%81%E1%85%A9%E1%86%A8%E1%84%8C%E1%85%B5%E1%86%AF%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"\"딸이 사라졌어요.\" \r\n어린 딸이 사라졌다는 제보.\r\n딸을 잃은 엄마는 세상을 잃은 슬픔에 빠진다.\r\n모녀의 집은 최근 연이은 강도사건으로 떠들썩한 동네.\r\n그러던 중 범인으로 추정되는 자의 침입 흔적이\r\n모녀의 집에서 발견 된다.","minParticipantCount":2,"maxParticipantCount":7,"difficulty":4,"genre":"추리/드라마","point":"엑스파일러만의 특수기기를 이용하라","youtubeLink":"https://www.youtube.com/embed/rRs1cr81Bes","colorCode":"#ddbe88","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":7,"merchantId":2,"nameKo":"501동 사람들","nameEn":"Receiver Invitation","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/7/cde8c643-2cc3-4cc7-8faf-fe57a884b578_501%E1%84%83%E1%85%A9%E1%86%BC%20%E1%84%89%E1%85%A1%E1%84%85%E1%85%A1%E1%86%B7%E1%84%83%E1%85%B3%E1%86%AF%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/7/96c78b37-c562-4c92-aee8-26b4fb1ad1ec_501%E1%84%83%E1%85%A9%E1%86%BC%20%E1%84%89%E1%85%A1%E1%84%85%E1%85%A1%E1%86%B7%E1%84%83%E1%85%B3%E1%86%AF%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"1993년 부산의 목련아파트.\r\n\r\n참사를 막기 위해 현재의 엑스파일러가\r\n1993년 당일로 돌아가 비극을 막으려 한다.\r\n\r\n왜, 무엇 때문에 그 일은 발생한 것인가!\r\n과연 엑스파일러는 주민들을 구할 수 있을 것인가.","minParticipantCount":2,"maxParticipantCount":6,"difficulty":3,"genre":"미스테리/스릴러","point":"현재와 과거를 잇는 수단을 이용하라","youtubeLink":"https://www.youtube.com/embed/GrTlevgPxN0","colorCode":"#4c969f","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":9,"merchantId":2,"nameKo":"이도공간","nameEn":"Receiver Invitation","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/9/d40ba6f3-1616-45d8-959e-0b9d44913f06_%E1%84%8B%E1%85%B5%E1%84%83%E1%85%A9%E1%84%80%E1%85%A9%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/9/d9a2f06f-f0fd-42fa-ba94-8d7bb1629c6f_%E1%84%8B%E1%85%B5%E1%84%83%E1%85%A9%E1%84%80%E1%85%A9%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB.jpeg","timetable":null,"description":"깜빡-\r\n꿈인가, 현실인가?\r\n더욱 깊은 곳으로 이끄는 소리가 들리면\r\n나는 새가 되어 꿈속을 배회한다.\r\n모든것이 낯설고, 모든것이 익숙하다.\r\n나는 누구인가?\r\n나를 부르는 당신은 누구인가?","minParticipantCount":2,"maxParticipantCount":6,"difficulty":3,"genre":"판타지/서스펜스","point":"모든것이 뒤죽박죽, 꿈 속으로 침전한다.","youtubeLink":"https://www.youtube.com/embed/sQjDmZAh8HY","colorCode":"#8b79b4","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":11,"merchantId":1,"nameKo":"다나아종합병원","nameEn":"hospital","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/11/b1380195-165e-4ac8-ba55-b57328c231c0_%E1%84%83%E1%85%A1%E1%84%82%E1%85%A1%E1%84%8B%E1%85%A1%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/11/55e1b337-359f-4afb-b04e-d9d020fa46d9_%E1%84%83%E1%85%A1%E1%84%82%E1%85%A1%E1%84%8B%E1%85%A1%E1%84%8C%E1%85%B5%E1%86%AB%E1%84%8D%E1%85%A1.png","timetable":null,"description":"1998년 12월 11일 부산의 유명병원,\r\n\r\n다나아 종합병원.\r\n획기적인 특허 시술로 유명해진 이곳.\r\n이 병원의 스타 의사 병원장이\r\n미스테리한 모습으로 피를 흘리며\r\n죽은 채 발견되었다.\r\n\r\n이 종합병원에서 무슨 일이\r\n일어나고 있는 것일까?\r\n살인사건과 관련된 유력 용의자는 총 4명!","minParticipantCount":4,"maxParticipantCount":6,"difficulty":3,"genre":"미스테리/코믹추리","point":"5인이상참여시       용의자외에탐정역할이추가됩니다.       역할은랜덤뽑기또는임의로선택할수있습니다.","youtubeLink":"https://www.youtube.com/embed/j6thiZgryjU","colorCode":"#fff200","hasXKit":false,"isCrimeScene":true,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":12,"merchantId":4,"nameKo":"그믐달살인사건","nameEn":"Dark Moon","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/21/5ceb292d-11e1-4f42-bb7e-aa81cb778f7a_%E1%84%80%E1%85%B3%E1%84%86%E1%85%B3%E1%86%B7%E1%84%83%E1%85%A1%E1%86%AF%20%E1%84%89%E1%85%A1%E1%86%AF%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A1%E1%84%80%E1%85%A5%E1%86%AB%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/21/c436f8c0-89e1-4e98-afb6-43f0a20f53e6_%E1%84%80%E1%85%B3%E1%84%86%E1%85%B3%E1%86%B7%E1%84%83%E1%85%A1%E1%86%AF%20%E1%84%89%E1%85%A1%E1%86%AF%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A1%E1%84%80%E1%85%A5%E1%86%AB%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","timetable":null,"description":"2013년 2월 24일\r\n\r\n한 여성의 쓸쓸한 고독사가 접수되었다.\r\n약 한 달 만에 발견된 그녀, 죽음의 의문을 쫓다\r\n유일한 다잉 메시지가 발견되는데...\r\n이 메시지를 남긴 증거물을 판매한 수상한 가게로\r\n수사를 위해 찾아간다.","minParticipantCount":4,"maxParticipantCount":6,"difficulty":4,"genre":"추리수사/드라마","point":"그녀가 남긴 소지품 증거 휴대폰 문자내역을 조사 하시오.","youtubeLink":"","colorCode":"#e14179","hasXKit":true,"isCrimeScene":false,"useYn":false,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":13,"merchantId":4,"nameKo":"숨바꼭질 (60분)","nameEn":"HIDE & SEEK","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/13/5f1caeda-2435-46d4-8255-4b0a7c1a55e9_%E1%84%89%E1%85%AE%E1%86%B7%E1%84%87%E1%85%A1%E1%84%81%E1%85%A9%E1%86%A8%E1%84%8C%E1%85%B5%E1%86%AF%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/13/0481b530-7e42-44d8-a65b-4d4e896bae15_%E1%84%89%E1%85%AE%E1%86%B7%E1%84%87%E1%85%A1%E1%84%81%E1%85%A9%E1%86%A8%E1%84%8C%E1%85%B5%E1%86%AF%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"\"딸이 사라졌어요\" 어린 딸이 사라졌다는 제보.\r\n딸을 잃은 엄마는 세상을 잃은 슬픔에 빠진다.\r\n모녀의 집은 최근 연이은 강도사건으로 떠들썩한 동네.\r\n그러던 중 범인으로 추정되는 자의 침입 흔적이 모녀의 집에서 발견된다.","minParticipantCount":2,"maxParticipantCount":7,"difficulty":4,"genre":"추리/스릴러","point":"","youtubeLink":"https://www.youtube.com/embed/rRs1cr81Bes","colorCode":"#9a380e","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":14,"merchantId":4,"nameKo":"501동사람들 (70분)","nameEn":"apartment","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/14/ae6b4c69-2d82-415e-9027-bf2bc015082a_501%E1%84%83%E1%85%A9%E1%86%BC%20%E1%84%89%E1%85%A1%E1%84%85%E1%85%A1%E1%86%B7%E1%84%83%E1%85%B3%E1%86%AF%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/14/ba9f6dbf-0cf3-4e87-8adc-cd0ab96a6cdd_501%E1%84%83%E1%85%A9%E1%86%BC%20%E1%84%89%E1%85%A1%E1%84%85%E1%85%A1%E1%86%B7%E1%84%83%E1%85%B3%E1%86%AF%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"1993년 부산의 목련아파트.\r\n\r\n참사를 막기 위해 현재의 엑스파일러가 \r\n1993년 당일로 돌아가 비극을 막으려 한다.\r\n\r\n왜, 무엇 때문에 그 일은 발생한 것인가!\r\n과연 엑스파일러는 주민들을 구할 수 있을 것인가.","minParticipantCount":2,"maxParticipantCount":6,"difficulty":4,"genre":"드라마/서스펜스","point":"현재와 과거를 잇는 수단을 이용하라","youtubeLink":"https://www.youtube.com/embed/GrTlevgPxN0","colorCode":"#22c3a3","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":15,"merchantId":4,"nameKo":"제물의 밤 (60분)","nameEn":"COVEN","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/15/f25ee662-7ad7-48e1-8b81-44f902527269_%E1%84%8C%E1%85%A6%E1%84%86%E1%85%AE%E1%86%AF%E1%84%8B%E1%85%B4%E1%84%87%E1%85%A1%E1%86%B7%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/15/6bf602ae-025c-4a33-96e2-95663d4824d7_%E1%84%8C%E1%85%A6%E1%84%86%E1%85%AE%E1%86%AF%E1%84%8B%E1%85%B4%E1%84%87%E1%85%A1%E1%86%B7%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"서른 번의 낮과 밤이 지나고 \r\n큰 달이 뜨면 시험을 원하는 자들이 숲으로 모여든다.\r\n\r\n재앙의 불이 타오르고 \r\n두꺼비의 내장, 산양의 쓸개를 담은 낡은 솥이\r\n지옥의 죽처럼 끓으면 모두가 시험에 들것이니\r\n제물의 운명을 피해 갈 자는 누구인가?","minParticipantCount":2,"maxParticipantCount":7,"difficulty":3,"genre":"미스테리/판타지","point":"","youtubeLink":"https://www.youtube.com/embed/zgIB-tDE1WY","colorCode":"#02ac4b","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":16,"merchantId":4,"nameKo":"그남자 그여자","nameEn":"THE LOVE STORY","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/16/a9855325-699e-4b72-ba53-ca100d01f804_%E1%84%80%E1%85%B3%E1%84%82%E1%85%A1%E1%86%B7%E1%84%8C%E1%85%A1%20%E1%84%80%E1%85%B3%E1%84%8B%E1%85%A7%E1%84%8C%E1%85%A1%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/16/bcfa5838-f267-4a20-8c7a-21c3418094c2_%E1%84%80%E1%85%B3%E1%84%82%E1%85%A1%E1%86%B7%E1%84%8C%E1%85%A1%20%E1%84%80%E1%85%B3%E1%84%8B%E1%85%A7%E1%84%8C%E1%85%A1%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"나는 너를 사랑했고, 너도 나를 사랑했다.\r\n\r\n그러나 영원할 것 같던 사랑은 \r\n오해와 원망으로 빛을 잃고-\r\n우리는 서로의 손을 놓았다.\r\n\r\n만약 우리가 그때 다른 선택을 했더라면, \r\n지금 우린 함께일까?","minParticipantCount":2,"maxParticipantCount":7,"difficulty":3,"genre":"로맨스/드라마","point":"그들의 사랑과 추억은 당신의 것과 닮아있다.","youtubeLink":"https://www.youtube.com/embed/yiEpNZL2O1Y","colorCode":"#A26DA6","hasXKit":true,"isCrimeScene":false,"useYn":false,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":17,"merchantId":4,"nameKo":"기묘한날개짓 (60분)","nameEn":"BUTTERFLY","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/17/b4970cfa-109c-4e7e-bd01-7901e94d0c70_%E1%84%80%E1%85%B5%E1%84%86%E1%85%AD%E1%84%92%E1%85%A1%E1%86%AB%20%E1%84%82%E1%85%A1%E1%86%AF%E1%84%80%E1%85%A2%E1%84%8C%E1%85%B5%E1%86%BA%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/17/a0ceb4ed-9a2e-44d6-9330-7bce800b0652_%E1%84%80%E1%85%B5%E1%84%86%E1%85%AD%E1%84%92%E1%85%A1%E1%86%AB%E1%84%82%E1%85%A1%E1%86%AF%E1%84%80%E1%85%A2%E1%84%8C%E1%85%B5%E1%86%BA.jpeg","timetable":null,"description":"연이은 여대생들의 실종, \r\n유력한 용의자로 곤충학자인 B교수가 떠오르지만\r\n그의 연구실 앞 CCTV에는 여대생들이 멀쩡히 걸어 나오는 모습이 포착되며 \r\nB교수는 용의 선상에서 빠져나간다. \r\n\r\n그러나, \r\n분명 B교수에게 무언가 있음을 직감하고 \r\n엑스파일러들은 수사에 돌입하는데 -","minParticipantCount":2,"maxParticipantCount":7,"difficulty":3,"genre":"추리/스릴러","point":"엑스파일러만의 특수기기를 이용하라","youtubeLink":"https://www.youtube.com/embed/Pa7ZwkLbwxE","colorCode":"#099efb","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":18,"merchantId":3,"nameKo":"운필귀정","nameEn":"The Luck’s rotation","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/18/3991e091-93d5-444f-b5ee-459fb154856f_%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%91%E1%85%B5%E1%86%AF%E1%84%80%E1%85%B1%E1%84%8C%E1%85%A5%E1%86%BC%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/18/4a430493-9231-472c-98f1-0a71aaf22d9e_%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%91%E1%85%B5%E1%86%AF%E1%84%80%E1%85%B1%E1%84%8C%E1%85%A5%E1%86%BC%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","timetable":null,"description":"과거 2004년, 지하B상가에서\r\n원인미상 화재 사고가 일어나 폐허로 방치 되었는데.\r\n최근, 이 지하상가에 무단침입 신고가 자주 접수되고.\r\n\r\n공소시효가 70분남은 미제사건 현장에 \r\n엑스파일러가 신속하게 출동한다.","minParticipantCount":2,"maxParticipantCount":4,"difficulty":4,"genre":"추리/수사","point":"무단침입한 이들이 남김 미제사건 실마리 속으로...","youtubeLink":"","colorCode":"#fd5916","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":19,"merchantId":3,"nameKo":"베다바리","nameEn":"Full-Bonding","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/19/e597bfe5-5481-4f33-82e8-aa6503556ed8_%E1%84%87%E1%85%A6%E1%84%83%E1%85%A1%E1%84%87%E1%85%A1%E1%84%8B%E1%85%B5%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/19/27ac0f34-9fb5-4ecd-b002-facde0db89ea_%E1%84%87%E1%85%A6%E1%84%83%E1%85%A1%E1%84%87%E1%85%A1%E1%84%8B%E1%85%B5%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","timetable":null,"description":"답답한 도심을 떠나\r\n저가 국내 관광 패키지 상품을 구매한 당신.\r\n인적 없는 낯선 구설역 앞에 도착한\r\n당신과 일행들은 시간에 맞춰 픽업 온, 관광 패키지에서 준비한 승합차에 올라탔다.\r\n승합차 안에서 가이드가 건네준\r\n박카스를 나눠 마신 후 깊은 잠에 들게 되었는데...","minParticipantCount":2,"maxParticipantCount":5,"difficulty":5,"genre":"드라마/서스펜스","point":"낯설고 수상한 동네, 아무도 믿지 마시오","youtubeLink":"https://www.youtube.com/embed/mS0bbCx6cJM","colorCode":"#fd5916","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":20,"merchantId":3,"nameKo":"경성도박장","nameEn":"Kyungs casino","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/20/60cbd58f-16fb-400a-a849-44595f3ca04e_%E1%84%80%E1%85%A7%E1%86%BC%E1%84%89%E1%85%A5%E1%86%BC%E1%84%83%E1%85%A9%E1%84%87%E1%85%A1%E1%86%A8%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/20/5eb9fd8b-76c1-474d-8940-d5f9eda69d02_%E1%84%80%E1%85%A7%E1%86%BC%E1%84%89%E1%85%A5%E1%86%BC%E1%84%83%E1%85%A9%E1%84%87%E1%85%A1%E1%86%A8%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","timetable":null,"description":"한파가 몰아치는 1936년 1월 10일 경성,\r\n\r\n잘나가는 로얄호텔 도박장의 주인.\r\n도바쿠 유키가 뷔아피룸에서 \r\n살해된 채로 발견되었다.\r\n\r\n도바쿠 유키는 당일 저녁에도 '장미화투매'와 \r\n놀이판을 즐기는 등, 활발하게 활동을 해왔다.\r\n\r\n도박판에서의 예민한 돈놀음으로 인해\r\n쌓인 원한이었을까?\r\n살인사건과 관련된 용의자는 총 4명!","minParticipantCount":4,"maxParticipantCount":6,"difficulty":4,"genre":"시대극추리/드라마","point":"","youtubeLink":"https://www.youtube.com/embed/EHR6IwVBpLY","colorCode":"#FFF20C","hasXKit":false,"isCrimeScene":true,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":21,"merchantId":3,"nameKo":"그믐달살인사건","nameEn":"Dark Moon","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/21/5ceb292d-11e1-4f42-bb7e-aa81cb778f7a_%E1%84%80%E1%85%B3%E1%84%86%E1%85%B3%E1%86%B7%E1%84%83%E1%85%A1%E1%86%AF%20%E1%84%89%E1%85%A1%E1%86%AF%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A1%E1%84%80%E1%85%A5%E1%86%AB%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/21/c436f8c0-89e1-4e98-afb6-43f0a20f53e6_%E1%84%80%E1%85%B3%E1%84%86%E1%85%B3%E1%86%B7%E1%84%83%E1%85%A1%E1%86%AF%20%E1%84%89%E1%85%A1%E1%86%AF%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A1%E1%84%80%E1%85%A5%E1%86%AB%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","timetable":null,"description":"2014년 9월 23일\r\n그믐달이 뜬 밤.\r\n\r\n각종 미스테리한 사건이 \r\n끊이지 않는다는 마을이 있다는 제보를 받고\r\n인기 프로그램 ‘그곳이 알고싶다’ 팀이\r\n사전 답사 차 마을을 방문하였다.\r\n\r\n그날 밤 자정, 마을 답사를 하러 나간 \r\n‘그곳이 알고싶다’ 메인PD인 조PD가 \r\n나무에 목이 매달려 죽은 채로 발견되었다. \r\n\r\n4명의 용의자.\r\n그 중 조PD를 죽인 사람은 누구인가?","minParticipantCount":4,"maxParticipantCount":6,"difficulty":3,"genre":"미스터리/드라마","point":"","youtubeLink":"https://www.youtube.com/embed/9U80-SIWFvg","colorCode":"#fff20c","hasXKit":false,"isCrimeScene":true,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":22,"merchantId":3,"nameKo":"청림맨션","nameEn":"Blue Forest House","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/22/8e5082a1-5dfc-44f3-9dc5-839121b09f9f_%E1%84%8E%E1%85%A5%E1%86%BC%E1%84%85%E1%85%B5%E1%86%B7%E1%84%86%E1%85%A2%E1%86%AB%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/22/540760a3-72f5-4c27-92d6-b65ceb2a0121_%E1%84%8E%E1%85%A5%E1%86%BC%E1%84%85%E1%85%B5%E1%86%B7%E1%84%86%E1%85%A2%E1%86%AB%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%87%E1%85%A2%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.jpeg","timetable":null,"description":"1984년, 홍콩의 오래된 맨션. \r\n젊은 부부와 홀어머니가 이사왔다.\r\n그로부터 3주 뒤,\r\n부부 중 아내의 모습이 보이지 않는다.\r\n그들의 집 앞에서 풍기는 정체모를 냄새와\r\n\"아내가 나가는 걸 싫어해서요.\"\r\n남편의 의심스러운 변명.\r\n아내는 어디로 사라진걸까?","minParticipantCount":2,"maxParticipantCount":6,"difficulty":3,"genre":"미스테리/스릴러","point":"누구도 아내를 목격하지 못했다","youtubeLink":"https://www.youtube.com/embed/JjVGymtgWlw","colorCode":"#FD5916","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":23,"merchantId":2,"nameKo":"짜불성설","nameEn":"Nonsense","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/23/08570067-0dd2-4e08-b753-0e419b32e244_%E1%84%8D%E1%85%A1%E1%84%87%E1%85%AE%E1%86%AF%E1%84%89%E1%85%A5%E1%86%BC%E1%84%89%E1%85%A5%E1%86%AF%20%E1%84%86%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/23/da49dcf3-cf3c-465c-9a71-0c0b6f3cc264_long_homepage_poster_%E1%84%8D%E1%85%A1%E1%84%87%E1%85%AE%E1%86%AF%E1%84%89%E1%85%A5%E1%86%BC%E1%84%89%E1%85%A5%E1%86%AF.png","timetable":null,"description":"취업을 위해 서울에 홀로 상경한 아들이\r\n사라졌다는 실종 신고가 접수됐다.\r\n\r\n평소 자주 연락하던 아들이 갑자기 소식이 끊겼는데,\r\n\r\n초동수사의 중요함을 알기에 수사관들은 긴박하게\r\n\r\n청년이 묵고 있던, 고시텔로  향하게 된다…","minParticipantCount":2,"maxParticipantCount":6,"difficulty":4,"genre":"사건수사/잠입","point":"공소시효는 75분 (PLAY TIME)","youtubeLink":"","colorCode":"#d23255","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null},{"id":24,"merchantId":4,"nameKo":"인지상점 (70분)","nameEn":"The Cogntive Store","mainImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/12/1710c8d9-7eda-4805-8907-82f52049c3de_%E1%84%83%E1%85%A2%E1%84%8C%E1%85%B5%202.png","bgImagePath":"https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/12/c5e57fe4-5203-4ca3-84cc-fe5fcaeebdfb_%E1%84%83%E1%85%A2%E1%84%8C%E1%85%B5%203.png","timetable":null,"description":"2013년 2월 24일\r\n\r\n한 여성의 쓸쓸한 고독사가 접수되었다.\r\n약 한 달 만에 발견된 그녀, 죽음의 의문을 쫓다\r\n유일한 다잉 메시지가 발견되는데...\r\n이 메시지를 남긴 증거물을 판매한 수상한 가게로\r\n수사를 위해 찾아간다.","minParticipantCount":2,"maxParticipantCount":6,"difficulty":4,"genre":"추리수사/드라마","point":"그녀가 남긴 소지품 증거 휴대폰 문자내역을 조사 하시오.","youtubeLink":"","colorCode":"#e14179","hasXKit":true,"isCrimeScene":false,"useYn":true,"timeTableList":null,"priceList":null,"abilityList":null,"reservationList":null}]
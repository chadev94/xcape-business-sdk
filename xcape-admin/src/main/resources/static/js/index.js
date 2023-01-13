const merchantId = document.getElementById('merchantId');
const themeId = document.getElementById('themeId');
const themeName = document.getElementById('themeName');
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

const getThemeInformation = (id) => {
    axios.get(`/themes/${id}`).then((res) => {
        const {resultCode} = res.data;
        const theme = res.data.result;
        if (resultCode === 'SUCCESS') {
            document.themeInfo.action = `/themes/${theme.id}`;
            merchantId.value = theme.merchantId;
            themeId.value = theme.id;
            themeName.value = theme.name;
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
            bindingPriceInputs(GENERAL_PRICE_AREA, GENERAL_PERSON, GENERAL_PRICE, theme.generalPrice);
            bindingPriceInputs(OPEN_ROOM_PRICE_AREA, OPEN_ROOM_PERSON, OPEN_ROOM_PRICE, theme.openRoomPrice);
        }
    });
}

document.getElementById('button').addEventListener('click', () => {
    const form = document.querySelector('.needs-validation');

    if(form.checkValidity()) {
        const formData = new FormData(form);
        const themeImageFormData = new FormData(document.themeImage);

        let priceArray = [];
        for (let i = 0; i < document.getElementById('priceArea').childElementCount; i++) {
            priceArray[i] = {person: formData.getAll('person')[i], price: formData.getAll('price')[i]};
        }

        let param = {
            merchantId: formData.get('merchantId'),
            themeId: formData.get('id'),
            themeName: formData.get('name'),
            difficulty: formData.get('difficulty'),
            price: JSON.stringify(priceArray),
            description: formData.get('description'),
            reasoning: formData.get('reasoning'),
            observation: formData.get('observation'),
            activity: formData.get('activity'),
            teamwork: formData.get('teamwork'),
            minPersonnel: formData.get('minPersonnel'),
            maxPersonnel: formData.get('maxPersonnel'),
            genre: formData.get('genre'),
            point: formData.get('point'),
            hasXKit: formData.get('hasXKit'),
            isCrimeScene: formData.get('isCrimeScene'),
        };

        if (document.themeImage.mainImage.value !== '') {
            param.mainImage = themeImageFormData.get('mainImage');
        }
        if (document.themeImage.bgImage.value !== '') {
            param.bgImage = themeImageFormData.get('bgImage')
        }

        axios.post(`/merchants/${merchantId.value}/themes`, param, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then((res) => {
            console.log(res);
        });
    }

    form.classList.add('was-validated');
});

// 각 select 태그에 0 ~ 5 까지 만듦
let options = '';
for (let i = 0; i <= 5; i++) {
    if (i === 3) {
        options += `<option value="${i}" selected>${i}</option>`;
    } else {
        options += `<option value="${i}">${i}</option>`;
    }
}
document.querySelectorAll('.form-select').forEach((select) => {
   select.innerHTML = options;
});

// 테마 클릭 이벤트
const listGroup = document.querySelectorAll('.list-group button');
listGroup.forEach((list) => {
    list.addEventListener('click', () => {
        document.querySelector('.was-validated')?.classList.remove('was-validated');
        document.querySelector('.list-group .active')?.classList.remove('active');
        list.classList.add('active');
    });
});

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

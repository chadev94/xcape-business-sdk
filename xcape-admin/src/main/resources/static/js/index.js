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
            createPriceInputs(theme.price);
        }
    });
}

document.getElementById('button').addEventListener('click', () => {
    const form = document.querySelector('.needs-validation');
    if(form.checkValidity()) {
        console.log('check validate true');
    } else {
        console.log('check validate false');
    }
    form.classList.add('was-validated');
    const formData = new FormData(form);

    let priceArray = [];
    for (let i = 0; i < document.getElementById('priceArea').childElementCount; i++) {
        priceArray[i] = {person: formData.getAll('person')[i], price: formData.getAll('price')[i]};
    }

    const param = {
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

    // axios.put(document.themeInfo.action, param, {
    //         headers: {
    //             'Content-Type': 'multipart/form-data'
    //         }
    // }).then((res) => {
    //     console.log(res);
    // });
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

document.getElementById('addPriceButton').addEventListener('click', () => {
    const person = document.querySelectorAll('input[name="person"]');
    const personCount = parseInt(person[person.length - 1].value.replace(/,/g, '')) + 1 || 1;
    const priceTemplate = document.getElementById('price-template').innerHTML;
    const priceInput = priceTemplate.replaceAll('{priceAreaId}', `priceArea-${personCount}`)
                                    .replace('{personId}', `person-${personCount}`)
                                    .replace('{personValue}', personCount.toString())
                                    .replace('{priceId}', `price-${personCount}`)
                                    .replace('{priceValue}', '0');
    document.getElementById('priceArea').insertAdjacentHTML('beforeend', priceInput);
});

const deletePrice = (priceAreaId) => {
    const priceArea = document.getElementById('priceArea');
    if (priceArea.childElementCount < 2) {
        alert('가격 정보는 1개 이상이어야 합니다.');
    } else {
        document.getElementById(priceAreaId).remove();
    }
}

const createPriceInputs = (priceInfo) => {
    let priceInputs = '';
    const priceTemplate = document.getElementById('price-template').innerHTML;
    if (priceInfo) {
        const priceArray = JSON.parse(priceInfo);
        priceArray.forEach((item) => {
            priceInputs += priceTemplate.replaceAll('{priceAreaId}', `priceArea-${item.person}`)
                                        .replace('{personId}', `person-${item.person}`)
                                        .replace('{personValue}', item.person)
                                        .replace('{priceId}', `price-${item.person}`)
                                        .replace('{priceValue}', item.price);
        });
    } else {
        priceInputs = priceTemplate.replaceAll('{priceAreaId}', 'priceArea-0')
            .replace('{personId}', 'person-0')
            .replace('{personValue}', '0')
            .replace('{priceId}', 'price-0')
            .replace('{priceValue}', '0');
    }

    document.getElementById('priceArea').innerHTML = priceInputs;
}

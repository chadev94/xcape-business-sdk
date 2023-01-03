const merchantId = document.getElementById('merchantId');
const themeId = document.getElementById('themeId');
const themeName = document.getElementById('themeName');
const difficulty = document.getElementById('difficulty');
const price = document.getElementById('price');
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
            price.value = theme.price || 0;
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
    // const formData = new FormData(form);
    // axios.put(document.themeInfo.action, formData, {
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


/**
 * @param e 해당 dom 객체 or number
 * @returns {string} 3 자리마다 , 찍힌 문자열
 */
const formattingNumber = (e) => {
    if (typeof e === 'object') {
        if (e.target.value === '') {
            e.target.value = 0;
        } else {
            e.target.value = parseInt(e.target.value).toLocaleString();
        }
    } else if (typeof e === 'number' || typeof e === 'string') {
        return e.toLocaleString() || 0;
    }
}

/**
 * @param e 해당 dom 객체
 * @returns {string} 숫자만 입력된 문자열
 */
const onlyNumber = (e) => {
    e.target.value = e.target.value.replace(/\D/g, '');
    if (e.target.value.charAt(0) === '0') {
        e.target.value = e.target.value.substring(1, e.value.length);
    }
}

const formattingTime = (number) => {
    if (number < 10) {
        return `0${number}`;
    } else {
        return number;
    }
}

const popAlert = (icon, title, text, timer) => {
    return swal({
        icon: icon,
        title: title,
        text: text,
        timer: timer
    });
}

const moveToReservations = (li) => {
    location.href = li.getAttribute('value')
}

const interpolate = (str, params) => {
    let keys = Object.keys(params);
    let values = Object.values(params);
    return new Function(...keys, `return \`${str}\`;`)(...values);
};

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

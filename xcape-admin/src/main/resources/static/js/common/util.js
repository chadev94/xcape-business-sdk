
/**
 * @param e 해당 dom 객체 or number
 * @returns {string} 3 자리마다 , 찍힌 문자열
 */
const formattingNumber = (e) => {
    if (typeof e === 'object') {
        if (e.value === '') {
            e.value = 0;
        } else {
            e.value = parseInt(e.value).toLocaleString();
        }
    } else if (typeof e === 'number' || typeof e === 'string') {
        return e.toLocaleString() || 0;
    }
}

/**
 * @param {HTMLInputElement} e 해당 dom 객체
 * @returns {string} 숫자만 입력된 문자열
 */
const onlyNumber = (e) => {
    e.value = e.value.replace(/\D/g, '');
    if (e.value.charAt(0) === '0') {
        e.value = e.value.substring(1, e.value.length);
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

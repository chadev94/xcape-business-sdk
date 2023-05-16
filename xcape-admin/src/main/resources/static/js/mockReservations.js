document.querySelectorAll('.list-group-item').forEach(item => {
    item.addEventListener('click', () => {
        document.querySelector('.list-group-item.active')?.classList.remove('active');
        item.classList.add('active');
        const merchantId = item.dataset.merchantId;
        getThemeList(merchantId);
    });
});

const getThemeList = (merchantId) => {
    axios.get(`/merchants/${merchantId}/themes`).then(res => {
        console.log(res);
    });
}

$('#dateRangePicker').daterangepicker({
    locale: {
        format: "YYYY-MM-DD",
        separator: " ~ ",
        applyLabel: "확인",
        cancelLabel: "취소",
        fromLabel: "From",
        toLabel: "To",
        customRangeLabel: "Custom",
        weekLabel: "W",
        daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
        monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
    },
    minDate: new Date()
});


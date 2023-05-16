(function () {

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
            const {resultCode, result} = res.data;
            if (resultCode === SUCCESS) {
                let themeOptions = '<option value="">전체</option>';
                const themeOptionTemplate = document.querySelector('#themeOptionTemplate').innerHTML;

                result.forEach((item) => {
                    const themeId = item.id;
                    const themeName = item.nameKo;
                    themeOptions += interpolate(themeOptionTemplate, {themeId, themeName});
                });

                document.querySelector('#themeSelect').innerHTML = themeOptions;
            }
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

    document.querySelectorAll('.hour').forEach(hour => {
        hour.innerHTML = document.querySelector('#hourOptionsTemplate').innerHTML;
    });

    document.querySelectorAll('.minute').forEach(minute => {
        minute.innerHTML = document.querySelector('#minuteOptionsTemplate').innerHTML;
    });

    document.querySelector('#saveMockReservationButton').addEventListener('click', () => {
        const themeId = document.querySelector('#themeSelect').value;

        const {startDate, endDate} = $('#dateRangePicker').data('daterangepicker');
        const startHour = document.querySelector('#startHour').value;
        const startMinute = document.querySelector('#startMinute').value;
        const endHour = document.querySelector('#endHour').value;
        const endMinute = document.querySelector('#endMinute').value;

        const startTime = `${startHour}:${startMinute}`;
        const endTime = `${endHour}:${endMinute}`;

        if (startTime > endTime) {
            alert('시작시간이 더 큽니다.');
        } else {
            const param = {
                startDate: startDate.format('YYYY-MM-DD'),
                endDate: endDate.format('YYYY-MM-DD'),
                startTime: `${startHour}:${startMinute}`,
                endTime: `${endHour}:${endMinute}`
            };

            if (themeId) {
                param.themeId = themeId;
            } else {
                param.merchantId = document.querySelector('.list-group-item.active').dataset.merchantId;
            }


        }
    });
})();


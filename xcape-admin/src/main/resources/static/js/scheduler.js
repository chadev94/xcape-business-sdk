let schedulerDate = new Date();
schedulerDate.setDate(schedulerDate.getDate() + 30);

const turnOnScheduler = (btn) => {
    const merchantId = btn.value;
    popAlert(
        "warning",
        "스케줄러 작동",
        (schedulerDate.getMonth() + 1) + "월 " + schedulerDate.getDate() + "일 부터 스케줄러가 작동됩니다."
    )
        .then((res) => {
            if (res) {
                btn.classList.add('disabled');
                const prevHTML = btn.innerHTML;
                btn.innerHTML = loadingSpinner;
                axios.put("/schedulers/on?merchantId=" + merchantId)
                    .then((res) => {
                        if (res.data.resultCode === SUCCESS) {
                            location.reload();
                        } else {
                            popAlert('error', '실패', '요청에 실패했습니다.', 1500)
                                .then(() => {
                                    btn.innerHTML = prevHTML;
                                    btn.classList.remove('disabled');
                                });
                        }
                    });
            }
        });
}

const turnOffScheduler = (btn) => {
    const merchantId = btn.value;
    popAlert(
        "warning",
        "스케줄러 중단",
        (schedulerDate.getMonth() + 1) + "월 " + schedulerDate.getDate() + "일 부터 스케줄러가 중단됩니다."
    )
        .then((res) => {
            if (res) {
                btn.classList.add('disabled');
                const prevHTML = btn.innerHTML;
                btn.innerHTML = loadingSpinner;
                axios.put("/schedulers/off?merchantId=" + merchantId)
                    .then((res) => {
                        if (res.data.resultCode === SUCCESS) {
                            location.reload();
                        } else {
                            popAlert('error', '실패', '요청에 실패했습니다.', 1500)
                                .then(() => {
                                    btn.innerHTML = prevHTML;
                                    btn.classList.remove('disabled');
                                });
                        }
                    });
            }
        });
}

const changeSchedulerTime = (btn) => {
    const merchantId = btn.value;
    popAlert(
        "warning",
        "스케줄러 시간 변경",
        (schedulerDate.getMonth() + 1) + "월 " + schedulerDate.getDate() + "일 부터 스케줄러 시간이 변경됩니다."
    )
        .then(res => {
            if (res) {
                // TODO: 시간 셀렉트박스로 수정 후 api 호출 구현 axios.put("/schedulers?")
            }
        });
}
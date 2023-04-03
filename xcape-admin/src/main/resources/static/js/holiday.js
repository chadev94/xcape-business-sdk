const turnOnScheduler = (btn) => {
    popAlert("warning", "스케줄러 작동", "계속 하시겠습니까?", 10000)
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
    popAlert("warning", "스케줄러 중단", "계속 하시겠습니까?", 10000)
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
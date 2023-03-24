const adminHost = 'http://localhost:8000/';
const merchantId = document.getElementById("reservationList").getAttribute("value");
const clonedModal = document.getElementById("modal").cloneNode(true);
const reservationAlert = (isSuccessful, successText) => {
        return isSuccessful ?
            swal({
                    icon: 'success',
                    title: '성공',
                    text: successText,
                    timer: 1000
            }) :
            swal({
                    icon: 'error',
                    title: '실패',
                    text: '요청에 실패했습니다.',
                    timer: 1000
            })
}

const datePickerSet = (element) => {location.href = "/reservations?date=" + element.value + "&merchantId=" + merchantId};
const formatDate = (date) =>
    date.getFullYear() + "-" +
    ((date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1)) + "-" +
    ((date.getDate() + 1) > 9 ? (date.getDate() + 1) : "0" + (date.getDate() + 1));

const onMouseOver = (element) => {
        element.classList.add("text-primary");
}

const onMouseOut = (element) => {
        element.classList.remove("text-primary");
}

const moveToDetail = (element) => {
        let reservationId = element.parentElement.getAttribute("value");
        location.href = "/reservations/" + reservationId;
};

const example = {       //      TODO: 예약조회 응답 예시 구현 후 삭제할 것
        "resultCode": "SUCCESS",
        "resultMessage": null,
        "result": {
                "id": 179,
                "themeId": 5,
                "merchantId": 2,
                "themeName": "핑퐁핑퐁",
                "merchantName": "건대-엑스케이프",
                "date": "2023-03-19",
                "time": "01:20:00",
                "isReserved": false,
                "reservedBy": null,
                "phoneNumber": null,
                "participantCount": null,
                "roomType": null,
                "price": null
        }
};

// TODO: 모달, 예약 등록/수정/취소 구현
const openModal = (element) => {
        const reservationId = element.getAttribute("value")
        const modal = document.getElementById("modal");

        // 적용, 취소 버튼에 id 세팅
        document.getElementById("cancelBtn").setAttribute("value", reservationId);
        document.getElementById("confirmBtn").setAttribute("value", reservationId);

        axios.get(adminHost + "reservations/" + reservationId).then((res) => {
                console.log(res);

                if (res.data.resultCode === "SUCCESS") {
                        const reservation = res.data.result;
                        const participantInfoArr = document.getElementById("theme_" + reservation.themeId).getAttribute("data").split("~");
                        const minParticipantCount = participantInfoArr[0];
                        const maxParticipantCount = participantInfoArr[1];

                        modal.setAttribute("data", reservation.id.toString());
                        document.getElementById("cancelBtn").hidden = !reservation.isReserved;
                        const participantSelect = document.getElementById("participantCount");
                        for (let i = parseInt(minParticipantCount); i < parseInt(maxParticipantCount); i++) {
                                let count = i.toString()
                                let option = document.createElement("option");
                                option.setAttribute("value", count);
                                option.text = count;
                                participantSelect.appendChild(option);
                        }

                        if (reservation.isReserved) {
                                document.getElementById("modalTitle").innerText = "예약 수정";
                                document.getElementById("reservedBy").value = reservation.reservedBy;
                                document.getElementById("phoneNumber").value = reservation.phoneNumber;
                                document.getElementById("participantCount").value = reservation.participantCount;
                                document.getElementById("roomType").checked = reservation.roomType === "openRoom";

                        } else {
                                document.getElementById("modalTitle").innerText = "예약 등록";
                        }

                        $("#modal").modal('show');
                } else {
                        reservationAlert(false);
                }
        });
}

const closeModal = () => {
        document.getElementById("modal").remove();
        document.body.appendChild(clonedModal);
}

const confirmEdit = (element) => {
        const reservationId = element.getAttribute("value");
        axios.put(adminHost + "reservations/" + reservationId).then((res) => {
                if (res.data.resultCode === "SUCCESS") {
                        reservationAlert(true, '정상적으로 등록되었습니다.');
                } else {
                        reservationAlert(false);
                }
        });
        closeModal();
}

const cancelReservation = (element) => {
        const reservationId = element.getAttribute("value");
        axios.delete(adminHost + "reservations/" + reservationId).then((res) => {
                if (res.data.resultCode === "SUCCESS") {
                        reservationAlert(true, '정상적으로 취소되었습니다.')
                            .then(() => {
                                    location.reload();
                            });
                } else {
                        reservationAlert(false);
                }
        });
        closeModal();
}

$("#datePicker").text(formatDate(new Date()));
$('#datePicker')
    .datepicker({
        format: 'yyyy-mm-dd', //데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
        startDate: '', //달력에서 선택 할 수 있는 가장 빠른 날짜. 이전으로는 선택 불가능 ( d : 일 m : 달 y : 년 w : 주)
        endDate: '', //달력에서 선택 할 수 있는 가장 느린 날짜. 이후로 선택 불가 ( d : 일 m : 달 y : 년 w : 주)
        autoclose: true, //사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
        calendarWeeks: false, //캘린더 옆에 몇 주차인지 보여주는 옵션 기본값 false 보여주려면 true
        clearBtn: false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
        datesDisabled: [], //선택 불가능한 일 설정 하는 배열 위에 있는 format 과 형식이 같아야함.
        daysOfWeekDisabled: [], //선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
        daysOfWeekHighlighted: [], //강조 되어야 하는 요일 설정
        disableTouchKeyboard: false, //모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
        immediateUpdates: false, //사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
        multidate: false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
        multidateSeparator: ',', //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
        templates: {
            leftArrow: '&laquo;',
            rightArrow: '&raquo;',
        }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
        showWeekDays: true, // 위에 요일 보여주는 옵션 기본값 : true
        title: '', //캘린더 상단에 보여주는 타이틀
        todayHighlight: true, //오늘 날짜에 하이라이팅 기능 기본값 :false
        toggleActive: true, //이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
        weekStart: 0, //달력 시작 요일 선택하는 것 기본값은 0인 일요일
        language: 'ko', //달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
    })
    .on('changeDate', function (e) {
        /* 이벤트의 종류 */
        //show : datePicker가 보이는 순간 호출
        //hide : datePicker가 숨겨지는 순간 호출
        //clearDate: clear 버튼 누르면 호출
        //changeDate : 사용자가 클릭해서 날짜가 변경되면 호출 (개인적으로 가장 많이 사용함)
        //changeMonth : 월이 변경되면 호출
        //changeYear : 년이 변경되는 호출
        //changeCentury : 한 세기가 변경되면 호출 ex) 20세기에서 21세기가 되는 순간
    });


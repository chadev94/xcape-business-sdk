const adminHost = 'http://localhost:8000/';
const merchantId = document.querySelector("#reservationList").getAttribute("value");
const modalTemplate = document.querySelector('#modalTemplate').innerHTML;

const numbering = () => {
        const numberArea = document.querySelector('#numberArea');
        let maxLength = 0;
        document.querySelectorAll(".theme").forEach((theme) => {
                maxLength = Math.max(maxLength, theme.querySelectorAll(".reservation").length);
        });
        for (let i = 0; i < maxLength; i++) {
                const numberDiv = document.createElement("div");
                numberDiv.style.width = "60px";
                numberDiv.style.height = "78px";
                ['text-center', 'bg-dark', 'mb-1', 'align-items-center', 'd-flex', 'justify-content-center'].forEach((className) => {
                        numberDiv.classList.add(className);
                });
                numberDiv.textContent = 'Time' + (i + 1);
                numberArea.appendChild(numberDiv);
        }

}
document.addEventListener('DOMContentLoaded', numbering);

const datePickerSet = (element) => {location.href = "/reservations?date=" + element.value + "&merchantId=" + merchantId};

const onMouseOver = (element) => {
        element.classList.add("text-primary");
}
const onMouseOut = (element) => {
        element.classList.remove("text-primary");
}

// 예약 조회 후 모달 띄우기
const openModal = (element) => {
        const reservationId = element.getAttribute("value");

        axios.get(adminHost + "reservations/" + reservationId).then((res) => {
                if (res.data.resultCode === SUCCESS) {
                        const reservation = res.data.result;
                        const participantInfoArr = document.querySelector("#theme_" + reservation.themeId).getAttribute("data").split("~");
                        const minParticipantCount = participantInfoArr[0];
                        const maxParticipantCount = participantInfoArr[1];

                        document.querySelector('#modal').innerHTML = modalTemplate
                            .replace('${reservationId}', reservation.themeId.toString())
                            .replace('${modalTitle}', reservation.isReserved ? "예약 수정" : "예약 등록")
                            .replace('${reservedBy}', reservation.isReserved ? reservation.reservedBy : '')
                            .replace('${phoneNumber}', reservation.isReserved ? reservation.phoneNumber : '');

                        console.log(document.querySelector('#modal').innerHTML);

                        const participantSelect = document.querySelector('#participantCount');
                        for (let i = parseInt(minParticipantCount); i < parseInt(maxParticipantCount); i++) {
                                let count = i.toString()
                                let option = document.createElement("option");
                                option.setAttribute("value", count);
                                option.text = count;
                                participantSelect.appendChild(option);
                        }

                        if (reservation.isReserved) {
                                participantSelect.value = reservation.participantCount.toString();
                                document.querySelector('#roomType').checked = reservation.roomType === OPEN_ROOM;
                        }

                        // 적용 취소 버튼에 reservationId 셋팅
                        document.querySelector("#modal #cancelBtn").setAttribute("value", reservationId);
                        document.querySelector("#modal #confirmBtn").setAttribute("value", reservationId);

                        if (reservation.isReserved) {
                                document.querySelector("#modal #cancelBtn").classList.remove('d-none');
                        }

                        $('#modal').modal('show');
                } else {
                        popAlert('error', '실패', '요청에 실패했습니다.', 1500);
                }
        });
}

// 예약 등록/수정
const confirmEdit = (element) => {
        const reservationId = element.getAttribute("value");
        const reservation = {
                id: reservationId,
                reservedBy: document.getElementById("reservedBy").value,
                phoneNumber: document.getElementById("phoneNumber").value,
                participantCount: parseInt(document.getElementById("participantCount").value),
                roomType: document.getElementById("roomType").checked ? OPEN_ROOM : GENERAL
        };

        Object.keys(reservation).forEach((key) => {
                if (reservation[key]) {
                        document.getElementById(key).classList.remove("is-invalid");
                        document.getElementById(key).classList.add("is-valid");
                } else {
                        document.getElementById(key).classList.remove("is-valid");
                        document.getElementById(key).classList.add("is-invalid");
                }
        });

        const invalidList = document.querySelectorAll(".is-invalid");
        if (invalidList.length > 0) {
                popAlert('warning', '실패', '필수 값이 누락되었습니다.', 1500);
        } else {
                reserve(reservation).then((res) => {
                        if (res.data.resultCode === SUCCESS) {
                                popAlert('success', '성공', '정상적으로 등록되었습니다.', 1500)
                                    .then(() => {
                                            location.reload();
                                    });
                        } else {
                                popAlert('error', '실패', '요청에 실패했습니다.', 1500);
                        }
                });
        }
}

// 예약 취소
const cancelReservation = (element) => {
        const reservationId = element.getAttribute("value");
        axios.delete(adminHost + "reservations/" + reservationId).then((res) => {
                if (res.data.resultCode === SUCCESS) {
                        popAlert('success', '성공', '정상적으로 등록되었습니다.', 1500)
                            .then(() => {
                                    location.reload();
                            });
                } else {
                        popAlert('error', '실패', '요청에 실패했습니다.', 1500);
                }
        });
}

// 일괄 선택 스위치 on/off 시 TODO: review
const changeBatchSwitch = (element) => {
        if (element.checked) {
                document.querySelectorAll('.not-reserved').forEach((element) => {
                        element.addEventListener('mouseover', (event) => {
                                element.classList.add('opacity-25');
                        });
                        element.addEventListener('mouseout', (event) => {
                                element.classList.remove('opacity-25');
                        });
                });
                document.querySelectorAll('.reservation-btn').forEach((element) => {
                        element.classList.add('d-none');
                });
        } else {
                document.querySelectorAll('.not-reserved').forEach((element) => {
                        element.removeEventListener('mouseover', (event) => {
                                element.classList.add('opacity-25');
                        });
                        element.removeEventListener('mouseout', (event) => {
                                element.classList.remove('opacity-25');
                        });
                });
                document.querySelectorAll('.reservation-btn').forEach((element) => {
                        element.classList.add('d-none');
                });
        }
}

// 예약 영역 선택 시 가예약 checkbox 선택
const checkFakeCheckbox = (element) => {
        element.querySelector('input[type=checkbox]').checked = !element.querySelector('input[type=checkbox]').checked;
}


// 일괄 가예약
const bookFake = async () => {
        if (document.querySelector('#batchSelectSwitch').checked) {
                for (const element of document.querySelectorAll('.fake-reservation-checkbox input:checked')) {
                        await reserve(FAKE_RESERVATION(element.getAttribute('value')));
                }
                location.reload();
        }
}

const reserve = (reservation) => {
        return axios.put(
            adminHost + "reservations/" + reservation.id
            + "?reservedBy=" + reservation.reservedBy
            + "&phoneNumber=" + reservation.phoneNumber
            + "&participantCount=" + reservation.participantCount
            + "&roomType=" + reservation.roomType
        );
}

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

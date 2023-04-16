const GENERAL_PRICE_AREA = 'generalPriceArea';
const OPEN_ROOM_PRICE_AREA = 'openRoomPriceArea';
const SUCCESS = 'SUCCESS';
const GENERAL = 'general';
const OPEN_ROOM = 'openRoom';
const SLIDER = 'SLIDER';
const BLOCK = 'BLOCK';
const CREATE = 'CREATE';
const UPDATE = 'UPDATE';
const SAVE_SUCCESS = '성공적으로 저장했습니다. 👏';
const SAVE_FAIL = '저장 중 에러가 발생했습니다. 😭';

const FAKE_RESERVATION = (reservationId) => {
    return {
        id: reservationId,
        reservedBy: 'XCAPE',
        phoneNumber: '01000000000',
        participantCount: 2,
        roomType: 'general'
    }
}


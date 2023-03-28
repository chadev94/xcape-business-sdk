const GENERAL_PRICE_AREA = 'generalPriceArea';
const OPEN_ROOM_PRICE_AREA = 'openRoomPriceArea';
const SUCCESS = 'SUCCESS';
const GENERAL = 'general';
const OPEN_ROOM = 'openRoom';

const FAKE_RESERVATION = (reservationId) => {
    return {
        id: reservationId,
        reservedBy: 'XCAPE',
        phoneNumber: '01000000000',
        participantCount: 2,
        roomType: 'general'
    }
}


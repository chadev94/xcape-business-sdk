const bannerTemplate = document.querySelector('#bannerTemplate').innerHTML;
const bannerModal = new bootstrap.Modal(document.querySelector('#bannerModal'));
const spinner = document.querySelector('#spinnerTemplate').innerHTML;

document.querySelectorAll('button.list-group-item').forEach(merchant => {
    merchant.addEventListener('click', (e) => {
        const {classList, dataset} = e.currentTarget;

        document.querySelectorAll('button.list-group-item').forEach(merchant => merchant.classList.remove('active'));
        classList.add('active');

        const merchantId = dataset.merchantId;
        getBannerList(merchantId);
        clearValidity();
    });
});

document.querySelector('#bannerSaveButton').addEventListener('click', (e) => {
    const bannerImageList = document.querySelectorAll('#bannerImageList img');
    const sliderBannerList = document.querySelectorAll('#sliderBannerList img');
    const blockBannerList = document.querySelectorAll('#blockBannerList img');

    const params = [];

    bannerImageList.forEach((bannerImage, index) => {
        const {bannerId} = bannerImage.dataset;
        params.push({
            id: bannerId,
            type: null,
            sequence: index,
            useYn: false
        });
    });

    sliderBannerList.forEach((sliderBanner, index) => {
        const {bannerId} = sliderBanner.dataset;
        params.push({
            id: bannerId,
            type: SLIDER,
            sequence: index,
            useYn: true
        });
    });

    blockBannerList.forEach((blockBanner, index) => {
        const {bannerId} = blockBanner.dataset;
        params.push({
            id: bannerId,
            type: BLOCK,
            sequence: index,
            useYn: true
        });
    });

    const bannerSaveButton = document.querySelector('#bannerSaveButton');
    bannerSaveButton.disabled = true;
    bannerSaveButton.innerHTML = spinner;

    const {merchantId} = document.querySelector('button.list-group-item.active').dataset;
    axios.put(`/merchants/${merchantId}/banners`, params).then(res => {
        alert(SAVE_SUCCESS);
        bannerSaveButton.disabled = false;
        bannerSaveButton.innerHTML = '저장';
    });
});

document.querySelector('#bannerUploadButton').addEventListener('click', () => {
    const {merchantId} = document.querySelector('button.list-group-item.active').dataset;
    const form = document.querySelector('.needs-validation');

    if (form.checkValidity()) {
        const bannerFormData = new FormData(form);
        const bannerId = document.bannerForm.id.value;

        const bannerUploadButton = document.getElementById('bannerUploadButton');
        bannerUploadButton.disabled = true;
        bannerUploadButton.innerHTML = spinner;

        if (bannerId !== '') {
            axios.put(`/banners/${bannerId}`, bannerFormData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then((res) => {
                if (res.data.resultCode === SUCCESS) {
                    alert(SAVE_SUCCESS);
                } else {
                    alert(SAVE_FAIL);
                }
            }).finally(() => {
                bannerUploadButton.disabled = false;
                bannerUploadButton.innerHTML = '저장';
                bannerModal.hide();
                document.querySelector('button.list-group-item.active').click();
            });
        } else {
            axios.post(`/merchants/${merchantId}/banners`, bannerFormData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then((res) => {
                if (res.data.resultCode === SUCCESS) {
                    alert(SAVE_SUCCESS);
                } else {
                    alert(SAVE_FAIL)
                }
            }).finally(() => {
                bannerUploadButton.disabled = false;
                bannerUploadButton.innerHTML = '저장';
                bannerModal.hide();
                document.querySelector('button.list-group-item.active').click();
            });
        }
    }

    form.classList.add('was-validated');
});

document.querySelector('#openBannerModal').addEventListener('click', () => {
    openModal(CREATE);
});

const getBannerList = (merchantId) => {
    axios.get(`/merchants/${merchantId}/banners`).then(res => {
        const {result, resultCode} = res.data;
        if (resultCode === SUCCESS) {
            const bannerList = result.filter(banner => banner.type === null);
            const sliderBannerList = result.filter(banner => banner.type === SLIDER);
            const blockBannerList = result.filter(banner => banner.type === BLOCK);

            sliderBannerList.sort((prev, next) => {
                return prev.sequence - next.sequence;
            });

            blockBannerList.sort((prev, next) => {
                return prev.sequence - next.sequence;
            });

            let bannerHtml = '';
            let sliderBannerHtml = '';
            let blockBannerHtml = '';
            bannerList.forEach(banner => {
                const {id: bannerId, imagePath} = banner;
                bannerHtml += interpolate(bannerTemplate, {bannerId, imagePath});
            });

            sliderBannerList.forEach(banner => {
                const {id: bannerId, imagePath} = banner;
                sliderBannerHtml += interpolate(bannerTemplate, {bannerId, imagePath});
            });

            blockBannerList.forEach(banner => {
                const {id: bannerId, imagePath} = banner;
                blockBannerHtml += interpolate(bannerTemplate, {bannerId, imagePath});
            });

            document.querySelector('#bannerImageList').innerHTML = bannerHtml;
            document.querySelector('#sliderBannerList').innerHTML = sliderBannerHtml;
            document.querySelector('#blockBannerList').innerHTML = blockBannerHtml;
            document.querySelectorAll('.bannerImage img').forEach(banner => {
                banner.addEventListener('click', (e) => {
                    const {bannerId} = e.currentTarget.dataset;
                    openModal(UPDATE, bannerId);
                });
            });
        }
    });
}

const openModal = (mode, bannerId) => {
    bannerModal.show();
    const bannerImagePreview = document.querySelector('#bannerImagePreview');
    if (mode === CREATE) {
        document.bannerForm.id.value = '';
        document.bannerForm.link.value = '';
        document.bannerForm.description.value = '';
        document.bannerForm.bannerImage.value = '';
        document.bannerForm.bannerImage.required = true;
        bannerImagePreview.src = '/images/noPhoto.jpg';
    } else if(mode === UPDATE) {
        axios.get(`/banners/${bannerId}`).then(res => {
            const {result, resultCode} = res.data;
            if (resultCode === SUCCESS) {
                const {id, link, description, imagePath} = result;
                document.bannerForm.id.value = id;
                document.bannerForm.link.value = link;
                document.bannerForm.description.value = description;
                document.bannerForm.bannerImage.value = '';
                document.bannerForm.bannerImage.required = false;
                bannerImagePreview.src = imagePath;
            }
        });
    }
    clearValidity();
}

const clearValidity = () => {
    document.bannerForm.classList.remove('was-validated');
}

const sliderBannerList = document.querySelector('#sliderBannerList');
Sortable.create(sliderBannerList, {
    group: 'shared',
    animation: 150
});

const blockBannerList = document.querySelector('#blockBannerList');
Sortable.create(blockBannerList, {
    group: 'shared',
    animation: 150
});

const bannerImageList = document.querySelector('#bannerImageList');
Sortable.create(bannerImageList, {
    group: {
        name: 'shared',
    },
    animation: 150
});

const firstMerchant = document.querySelector('button.list-group-item');
firstMerchant?.classList.add('active');
firstMerchant?.click();


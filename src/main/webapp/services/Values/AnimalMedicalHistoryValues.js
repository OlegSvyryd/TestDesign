angular.module('AnimalMedicalHistoryValues', [])
    .value('AnimalMedicalHistoryValues', {
        filter: {
            page: 1,
            limit: '10',
            animal: {}
        },
        totalItems: {count: 0},
        items: {values: []},
        item: {},
        itemTypes: {values: []}
    });
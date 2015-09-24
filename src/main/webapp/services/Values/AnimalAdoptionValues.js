angular.module('AnimalAdoptionValues', [])
    .value('AnimalAdoptionValues', {
        filter: {page: 1, limit: '10'},
        totalItems: {count: 0},
        animals: {values: []},
        animalTypes: {values: []},
        animalServices: {values: []},
        animalStatuses: {}
    });

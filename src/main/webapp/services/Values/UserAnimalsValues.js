angular.module('UserAnimalsValues', [])
    .value('UserAnimalsValues', {
        filter: {page: 1, limit: '10'},
        totalItems: {count: 0},
        animals: {values: []},
        animalTypes: {values: []},
        animalServices: {values: []},
        animal: {}
    });
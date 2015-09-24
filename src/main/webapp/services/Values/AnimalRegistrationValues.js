animalRegistrationModule
    .value('AnimalRegistrationValues', {
        animalTypes: {values: []},
        animalServices: {values: []},
        dateOfRegister:  {
            now: function(){
                var today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth(); //January is 0!
                var yyyy = today.getFullYear();

                return new Date(Date.UTC(yyyy, mm, dd));
            }()
        },
        address: {
                country:'',
                town:'',
                street:'',
                index:''
        }
    });


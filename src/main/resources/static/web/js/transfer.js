Vue.createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            allTransactions: [],
            accountOrigenValue: 'all',
            destiny: '',
            amount: 0.0,
            description: '',
        }
    },

    created() {
        axios.get(`/api/clients/current`)
            .then(data => {
                this.clients = data.data
                this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id)
                this.accounts.forEach(account => {
                    account.transaction.forEach(transaction => this.allTransactions.push(transaction))
                })
                this.allTransactions.sort((a, b) => b.id - a.id)
            })
    },

    methods: {
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = './web/login.html')
                .catch(error => alert(error.message = 'No se pudo desconectar.'))
        },

        transfer() {
            axios.post('/api/clients/current/transactions', `amount=${this.amount}&description=${this.description}&accountOrigenNumber=${this.accountOrigenValue}&accountDestinyNumber=${this.destiny}`)
                .then(response => {
                    window.location.href = '/web/accounts.html'
                    console.log('transferencia completa')
                })
                .catch(function (error) {
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        console.log(error.response.data);
                        console.log(error.response.status);
                        console.log(error.response.headers);
                    } else if (error.request) {
                        // The request was made but no response was received
                        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
                        // http.ClientRequest in node.js
                        console.log(error.request);
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        console.log('Error', error.message);
                    }
                    console.log(error.config);
                });
        }
    },

    computed: {

    }

}).mount('#app')
Vue.createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            transactions: [],
            loans: [],
            cardsCREDIT: [],
            cardsDEBIT: [],
            type: '',
            color: '',
        }
    },

    created() {
        axios.get(`/api/clients/current`)
            .then(data => {
                this.clients = data.data
                this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id)
                this.transactions = this.accounts.transactions
                this.loans = this.clients.loan.sort((a, b) => a.id - b.id)
                this.cardsCREDIT = this.clients.card.filter(card => card.type == 'CREDIT' ? this.cardsCREDIT.push(card) : null)
                this.cardsDEBIT = this.clients.card.filter(card => card.type == 'DEBIT' ? this.cardsDEBIT.push(card) : null)
                this.cardsCREDIT.sort((a, b) => a.id - b.id)
                this.cardsDEBIT.sort((a, b) => a.id - b.id)
                console.log(this.clients)
                console.log(this.accounts)
                console.log(this.transactions)
                console.log(this.loans)
                console.log(this.cardsCREDIT)
                console.log(this.cardsDEBIT)
            })
    },

    methods: {
        createCard() {
            axios.post('/api/clients/current/cards', `cardColor=${this.color}&cardType=${this.type}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => window.location.href = '/web/card.html')
                .catch(error => alert(error.message = 'No se pudo crear.'))
        },
    },

    computed: {

    }

}).mount('#app')
Vue.createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            clientLoans: [],
            loans: [],
            typeLoan: {},
            // payment: '',
            payments: 0,
            amount: 0,
            accountNumber: '',
        }
    },

    created() {

        axios.get(`/api/clients/current`)
            .then(data => {
                this.clients = data.data
                this.accounts = this.clients.accounts
                this.clientLoans = this.clients.loan
                console.log(this.clients)
                console.log(this.clientLoans)
                axios.get(`/api/loans`)
                    .then(data => {
                        this.loans = data.data
                        console.log(this.loans)
                    })
                    console.log(this.payments)
            })
    },

    methods: {
        selectLoan(loan){
            this.typeLoan = loan
            console.log(loan)
        },

        requestLoan(){
            let applyForLoan = {
                idLoan: this.typeLoan.id,
                amount: this.amount,
                payment: this.payments,
                accountNumber: this.accountNumber,
            }
            axios.post('/api/loans', applyForLoan)
            .then(response => {
                console.log('creado')
                window.location.href = "/web/accounts.html"
            })
        }
    },

    computed: {

    }

}).mount('#app')
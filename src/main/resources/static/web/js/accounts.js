Vue.createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            allTransactions: [],
            eightTransactions: [],
            totalBalance: 0,
            loans: [],
            cardsCREDIT: [],
            cardsDEBIT: [],
            allCards: [],
            titanium: '',
            gold: '',
            silver: '',
            credit: '',
            debit: '',
        }
    },

    created() {
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(data => {
                this.clients = data.data
                this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id)
                this.accounts.forEach(account => {
                    account.transaction.forEach(transaction => this.allTransactions.push(transaction))
                })  
                this.accounts.forEach(account => {
                    this.totalBalance += account.balance
                })
                this.allTransactions.sort((a, b) => b.id - a.id)
                this.loans = this.clients.loan.sort((a, b) => a.id - b.id)
                this.clients.card.forEach(card => this.allCards.push(card))
                console.log(this.clients)
                console.log(this.accounts)
                console.log(this.allTransactions)
                console.log(this.totalBalance)
                console.log(this.loans)
                axios.get(`http://localhost:8080/api/clients/current/cards`)
                .then(data => {
                    this.allCards = data.data
                    this.cardsCREDIT = this.clients.card.filter(card => card.type == 'CREDIT' ? this.cardsCREDIT.push(card) : null)
                    this.cardsDEBIT = this.clients.card.filter(card => card.type == 'DEBIT' ? this.cardsDEBIT.push(card) : null)
                    this.allCards.sort((a, b) => a.id - b.id)
                    this.cardsCREDIT.sort((a, b) => a.id - b.id)
                    this.cardsDEBIT.sort((a, b) => a.id - b.id)
                    console.log(this.cardsCREDIT)
                    console.log(this.cardsDEBIT)
                    console.log(this.allCards)
                })
            })
    },

    methods: {
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = 'http://localhost:8080/web/login.html')
                .catch(error => alert(error.message = 'No se pudo desconectar.'))
        },

        newAccount(){
            axios.post('/api/clients/current/accounts')
            .then(response => {
                window.location.reload()
            })
        },

        toTransaction(){
            window.location.href = './transfers.html'
        },

        toCreateCards(){
            window.location.href = './create-cards.html'
        },

        buttonChevronFocus(){
            let buttonChevronFocus = document.querySelector('#chevronRight')
            buttonChevronFocus.classList.remove('chevronRightUnFocus')
            buttonChevronFocus.classList.add('chevronRightFocus')
        },
        buttonChevronUnFocus(){
            let buttonChevronUnFocus = document.querySelector('#chevronRight')
            buttonChevronUnFocus.classList.remove('chevronRightFocus')
            buttonChevronUnFocus.classList.add('chevronRightUnFocus')
        },

        cardExpired(thruDate){
            let today = new Date
            let month = ''
            if((today.getMonth()+1) < 10){
                month = '0' + (today.getMonth()+1)
            }else{
                month = (today.getMonth()+1).toString()
            }
            let date = month + '/' + today.getFullYear().toString().slice(-2)
            console.log(typeof today)
            console.log(typeof date)
            console.log(date)
            if(date === thruDate){
                return true
            }
            return false
        }
    },

    computed: {
    }

}).mount('#app')
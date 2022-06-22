Vue.createApp({
    data() {
        return {
            client: [],
            account: [],
            transactions: [],
            transactionsDEBIT: [],
            transactionsCREDIT: [],
        }
    },

    created() {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        axios.get(`/api/clients/current`)
            .then(data => {
                this.client = data.data
                this.account = data.data.accounts.filter(account => account.id == `${id}`)
                this.transactions = this.account[0].transaction
                this.transactionsDEBIT = this.transactions.filter(transaction => transaction.type == 'DEBIT').sort((a, b) => a.id - b.id)
                this.transactionsCREDIT = this.transactions.filter(transaction => transaction.type == 'CREDIT').sort((a, b) => a.id - b.id)
                console.log(this.account)
                console.log(this.transactions)
                console.log(this.transactionsDEBIT)
                console.log(this.transactionsCREDIT)
                this.graphic()
            })
    },

    methods: {
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = '/web/login.html')
                .catch(error => alert(error.message = 'No se pudo desconectar.'))
        },

        graphic() {
            $("#chart-container").insertFusionCharts({
                type: "doughnut2d",
                width: "100%",
                height: "100%",
                dataFormat: "json",
                dataSource: {
                    chart: {
                        enableSmartLabels: false,
                        showpercentvalues: "1",
                        defaultcenterlabel: "Account",
                        aligncaptionwithcanvas: "0",
                        captionpadding: "0",
                        decimals: "0",
                        plottooltext:
                            "<b>$percentValue</b> of our transactions are <b>$label</b>",

                        theme: "fusion"
                    },
                    data: [
                        {
                            color: "#29577b",
                            label: "Incomes",
                            value: this.transactionsCREDIT.map(transaction => transaction.amount).reduce((a,b) => a + b, 0)
                        },
                        {
                            color: "#35c09c",
                            label: "Expenses",
                            value: this.transactionsDEBIT.map(transaction => transaction.amount).reduce((a,b) => a + b, 0)
                        },
                    ]
                },
            });
        },

        occultBalance(){
            let accountBalance = document.querySelector('.accountBalance')
            let openEye = document.querySelector('.openEye')
            let closedEye = document.querySelector('.closedEye')
            openEye.classList.add('eyeNone')
            closedEye.classList.remove('eyeNone')
        },
        showBalance(){
            let accountBalance = document.querySelector('.accountBalance')
            let openEye = document.querySelector('.openEye')
            let closedEye = document.querySelector('.closedEye')
            openEye.classList.remove('eyeNone')
            closedEye.classList.add('eyeNone')
        }
    },

    computed: {

    },

    mounted(){

        setTimeout(() => {
            let chartContainer = document.getElementById('chart-container')
            let span = chartContainer.children
            console.log(span)
            console.log({chartContainer}.chartContainer.children)
        }, 5000);
    }

}).mount('#app')



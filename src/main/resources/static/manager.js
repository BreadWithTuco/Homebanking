Vue.createApp({
    data() {
        return {
            count: 0,
            data: [],
            firstName: '',
            lastName: '',
            email: '',
            client: {},
            accounts: [],
            transactions: [],
        }
    },

    created() {
        axios.get(`/api/clients`)
            .then(data => {
                this.data = data.data
                console.log(this.data)
            })
    },

    methods: {
        agregarCliente() {
            if (this.firstName != '' && this.lastName != '' && this.email != '' && this.email.includes('@' && '.com')) {
                this.client = {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                    accounts: this.accounts,
                    transactions: this.transactions,
                }
            }
            axios.post(`/api/clients`, this.client)
        },
    },

    computed: {

    }

}).mount('#app')
Vue.createApp({
  data() {
    return {
      clients: [],
      loginEmail: '',
      loginPassword: '',
      registerFirstName: '',
      registerLastName: '',
      registerEmail: '',
      registerPassword: '',
    }
  },

  created() {



  },

  methods: {
    login() {
      axios.post('/api/login', `email=${this.loginEmail}&password=${this.loginPassword}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(response => window.location.href = '/web/accounts.html')
        .catch(error => alert('Error al iniciar sesion'))
    },

    register() {
      axios.post('/api/clients', `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(response => {
          this.loginEmail = this.registerEmail
          this.loginPassword = this.registerPassword
          this.login()
        })
        .catch(error => alert(error.message = 'Error al crear la sesion.'))
    },
  },

  computed: {

  }

}).mount('#app')

const switchers = [...document.querySelectorAll(".switcher")];

switchers.forEach((item) => {
  item.addEventListener("click", function () {
    switchers.forEach((item) =>
      item.parentElement.classList.remove("is-active")
    );
    this.parentElement.classList.add("is-active");
  });
});
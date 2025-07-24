<template>
    <div class="get-involved-page">
        <!-- Header Component -->
        <header>
            <div class="container_12">
                <div class="grid_12">
                    <h1>
                        <router-link to="/">
                            <img src="../assets/images/logo.png" alt="Your Happy Family">
                        </router-link>
                    </h1>
                    <div class="menu_block">
                        <a href="#" class="donate">DONATE</a>
                        <div class="clear"></div>
                        <nav class="horizontal-nav full-width">
                            <ul class="sf-menu">
                                <li><router-link to="/">Home</router-link></li>
                                <li><router-link to="/what-we-do">What We Do</router-link></li>
                                <li><router-link to="/media">Media</router-link></li>
                                <li class="current"><router-link to="/get-involved">Get Involved</router-link></li>
                                <li><router-link to="/contacts">Contacts</router-link></li>
                            </ul>
                        </nav>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <div class="content">
            <div class="ic">More Website Templates @ TemplateMonster.com - March 24, 2014!</div>
            <div class="container_12">
                <div class="gallery">
                    <div v-for="(item, index) in galleryItems" :key="index" class="grid_3">
                        <h2>{{ item.title }}</h2>
                        <a :href="item.bigImage" class="gal">
                            <img :src="item.thumbImage" :alt="item.title">
                        </a>
                        {{ item.description }}
                        <br>
                        <a href="#" class="btn">Learn More</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bottom Block Component -->
        <div class="bottom_block">
            <div class="container_12">
                <div class="grid_4">
                    <h3>Stay Informed</h3>
                    <div class="text1">Subscribe to Our Newsletter</div>
                    <form @submit.prevent="submitNewsletter">
                        <div class="rel">
                            <div v-if="newsletterSuccess" class="success">Your subscribe request has been sent!</div>
                            <label class="email">
                                <input
                                        type="email"
                                        v-model="newsletterEmail"
                                        placeholder="Enter your Email"
                                        @blur="validateEmail"
                                        :class="{ error: emailError }"
                                >
                                <span v-if="emailError" class="error">*This is not a valid email address.</span>
                            </label>
                        </div>
                        <button type="submit" class="btn">Submit</button>
                    </form>
                </div>
                <div class="grid_5 prefix_3">
                    <h3>Stay Connected</h3>
                    <div class="text1">Follow Us on Social Media Accounts</div>
                    <p>Cras facilisis, nulla vel viverra auctor, leo magna sodales felis, quis malesuada nibh odio ut velit. Proin pharetra</p>
                    <div class="socials">
                        <a href="#"><i class="fa fa-twitter"></i></a>
                        <a href="#"><i class="fa fa-facebook"></i></a>
                        <a href="#"><i class="fa fa-pinterest-square"></i></a>
                        <a href="#"><i class="fa fa-google-plus"></i></a>
                        <a href="#"><i class="fa fa-instagram"></i></a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer Component -->
        <footer>
            <div class="container_12">
                <div class="grid_12">
                    <div class="copy">
                        Life &copy; {{ currentYear }} | <a href="#">Privacy Policy</a> <br>
                        Website designed by <a href="http://www.templatemonster.com/" rel="nofollow">TemplateMonster.com</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</template>

<script>
    export default {
        name: 'GetInvolved',
        data() {
            return {
                currentYear: new Date().getFullYear(),
                newsletterEmail: '',
                emailError: false,
                newsletterSuccess: false,
                galleryItems: [
                    {
                        title: 'Aliquam nibh antd dictum',
                        bigImage: require('../assets/images/big1.jpg'),
                        thumbImage: require('../assets/images/page4_img1.jpg'),
                        description: 'Broin pharetra luctus diamesque eros convallikoaecenas vehicula egestas venenatis. Duis masser'
                    },
                    {
                        title: 'Praesent faucibus malesu',
                        bigImage: require('../assets/images/big2.jpg'),
                        thumbImage: require('../assets/images/page4_img2.jpg'),
                        description: 'Kroin pharetra luctus diamesque eros convallikoaecenas vehicula egestas venenatis. Duis mass'
                    },
                    // Add all other gallery items here...
                ]
            }
        },
        methods: {
            validateEmail() {
                const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                this.emailError = !re.test(this.newsletterEmail);
                return !this.emailError;
            },
            submitNewsletter() {
                if (this.validateEmail()) {
                    // Here you would typically send the email to your backend
                    console.log('Submitting:', this.newsletterEmail);
                    this.newsletterSuccess = true;
                    this.newsletterEmail = '';
                    setTimeout(() => {
                        this.newsletterSuccess = false;
                    }, 3000);
                }
            }
        },
        mounted() {
            // Initialize any plugins that need to run after the component is mounted
            $('.gallery a.gal').touchTouch();
        }
    }
</script>

<style scoped>
    /* You would import these styles or include them in your main CSS */
    @import '../assets/css/form.css';
    @import '../assets/css/touchTouch.css';
    @import '../assets/css/style.css';

    /* Component-specific styles can go here */
    .get-involved-page {
        font-family: inherit;
    }

    .btn {
        cursor: pointer;
    }

    .socials a {
        margin-right: 10px;
        color: #333;
        text-decoration: none;
    }
</style>
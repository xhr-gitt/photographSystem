<template>
  <div class="contacts-page">
    <!-- 头部组件 -->
    <header>
      <div class="container_12">
        <div class="grid_12">
          <h1>
            <router-link to="/">
              <img src="../assets/images/logo.png" alt="幸福家庭摄影">
            </router-link>
          </h1>
          <div class="menu_block">
            <div class="clear"></div>
            <nav class="horizontal-nav full-width">
              <ul class="sf-menu">
                <li class="current">
                  <router-link to="/main" class="text-xl font-semibold text-gray-900">
                    返回首页
                  </router-link>
                </li>
                <li><router-link to="/appointment">预约拍摄</router-link></li>
                <li><router-link to="/ourPage">我们的影楼</router-link></li>
                <li><router-link to="/contact">关于我们</router-link></li>
              </ul>
            </nav>
            <div class="clear"></div>
          </div>
        </div>
      </div>
    </header>

    <!-- 主要内容 -->
    <div class="content">
      <div class="ic">更多网站模板 @ TemplateMonster.com</div>
      <div class="container_12">
        <div class="grid_12">
          <h2>联系我们</h2>
          <div class="map">
            <figure>
              <iframe src="https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=Brooklyn,+New+York,+NY,+United+States&amp;aq=0&amp;sll=37.0625,-95.677068&amp;sspn=61.282355,146.513672&amp;ie=UTF8&amp;hq=&amp;hnear=Brooklyn,+Kings,+New+York&amp;ll=40.649974,-73.950005&amp;spn=0.01628,0.025663&amp;z=14&amp;iwloc=A&amp;output=embed"></iframe>
            </figure>
            <div class="grid_4 alpha">
              <h2>地址：</h2>
              <address>
                <i class="fa fa-home"></i>
                北京市朝阳区幸福路138号 <br>
                中国 <br>
                邮编：100000
              </address>
            </div>
            <div class="grid_4">
              <h2>电话：</h2>
              <div class="m_phone">
                <i class="fa fa-phone"></i>
                +86 10 8888 8888
              </div>
              <div class="m_phone">
                <i class="fa fa-print"></i>
                +86 10 8888 9999
              </div>
            </div>
            <div class="grid_4 omega">
              <h2>邮箱：</h2>
              <a href="mailto:info@happyfamily.com"><i class="fa fa-envelope-o"></i> info@happyfamily.com</a>
            </div>
            <div class="clear"></div>
          </div>

          <h2>留言表单</h2>
          <p>
            我们为所有客户提供24/7全天候服务支持，随时为您解答疑问。
          </p>
          <p>
            如果您需要定制服务或有特殊需求，请通过以下表单联系我们。
          </p>
        </div>
      </div>
    </div>

    <!-- 底部区块 -->
    <div class="bottom_block">
      <div class="container_12">
        <div class="grid_4">
          <h3>订阅资讯</h3>
          <div class="text1">订阅我们的电子报</div>
          <form @submit.prevent="submitNewsletter" class="newsletter-form">
            <div class="rel">
              <div v-if="newsletterSuccess" class="success">订阅请求已发送成功！</div>
              <label class="email">
                <input
                    type="email"
                    v-model="newsletterEmail"
                    placeholder="请输入您的邮箱"
                    @blur="validateEmail"
                    :class="{ error: emailError }"
                >
                <span v-if="emailError" class="error">*请输入有效的邮箱地址</span>
              </label>
            </div>
            <button type="submit" class="btn">提交</button>
          </form>
        </div>
        <div class="grid_5 prefix_3">
          <h3>关注我们</h3>
          <div class="text1">关注我们的社交媒体账号</div>
          <p>通过社交媒体获取最新优惠活动、拍摄样片和客户评价等信息</p>
          <div class="socials">
            <a href="#"><i class="fa fa-weibo"></i></a>
            <a href="#"><i class="fa fa-weixin"></i></a>
            <a href="#"><i class="fa fa-qq"></i></a>
            <a href="#"><i class="fa fa-douyin"></i></a>
            <a href="#"><i class="fa fa-xiaohongshu"></i></a>
          </div>
        </div>
      </div>
    </div>

    <!-- 页脚 -->
    <footer>
      <div class="container_12">
        <div class="grid_12">
          <div class="copy">
            幸福家庭摄影 &copy; {{ currentYear }} | <a href="#">隐私政策</a> <br>
            网站设计由 <a href="http://www.templatemonster.com/" rel="nofollow">TemplateMonster.com</a> 提供
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'ContactsPage',
  data() {
    return {
      currentYear: new Date().getFullYear(),
      formSubmitted: false,
      formData: {
        name: '',
        email: '',
        subject: '',
        message: ''
      },
      errors: {
        name: '',
        email: '',
        subject: '',
        message: ''
      },
      newsletterEmail: '',
      emailError: false,
      newsletterSuccess: false
    }
  },
  methods: {
    validateField(field) {
      if (field === 'name') {
        this.errors.name = this.formData.name ? '' : '此项为必填';
      } else if (field === 'email') {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        this.errors.email = !this.formData.email
            ? '此项为必填'
            : (!emailRegex.test(this.formData.email) ? '邮箱格式不正确' : '');
      } else if (field === 'subject') {
        this.errors.subject = this.formData.subject ? '' : '此项为必填';
      } else if (field === 'message') {
        this.errors.message = !this.formData.message
            ? '此项为必填'
            : (this.formData.message.length < 20 ? '内容太简短' : '');
      }
    },
    validateForm() {
      Object.keys(this.formData).forEach(field => {
        this.validateField(field);
      });
      return !Object.values(this.errors).some(error => error);
    },
    submitContactForm() {
      if (this.validateForm()) {
        console.log('表单已提交:', this.formData);
        this.formSubmitted = true;
        setTimeout(() => {
          this.formSubmitted = false;
          this.resetForm();
        }, 3000);
      }
    },
    resetForm() {
      this.formData = {
        name: '',
        email: '',
        subject: '',
        message: ''
      };
      this.errors = {
        name: '',
        email: '',
        subject: '',
        message: ''
      };
    },
    validateEmail() {
      const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.emailError = !re.test(this.newsletterEmail);
      return !this.emailError;
    },
    submitNewsletter() {
      if (this.validateEmail()) {
        console.log('订阅邮箱:', this.newsletterEmail);
        this.newsletterSuccess = true;
        this.newsletterEmail = '';
        setTimeout(() => {
          this.newsletterSuccess = false;
        }, 3000);
      }
    }
  }
}
</script>

<style scoped>
/* 导入基础样式 */
@import '../assets/css/form.css';
@import '../assets/css/style.css';

/* 组件特定样式 */
.contacts-page {
  font-family: 'Microsoft YaHei', 'PingFang SC', sans-serif;
  color: #333;
  line-height: 1.6;
}

.map iframe {
  width: 100%;
  height: 400px;
  border: none;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

address, .m_phone {
  margin-bottom: 15px;
  line-height: 1.8;
  font-style: normal;
}

.fa {
  margin-right: 10px;
  color: #ff5a00;
  width: 20px;
  text-align: center;
}

.col1 {
  color: #ff5a00;
  text-decoration: none;
  transition: color 0.3s;
}

.col1:hover {
  color: #e04a00;
}

.contact-form {
  margin-top: 30px;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}

.error-message {
  color: #ff4d4f;
  font-size: 13px;
  display: block;
  margin-top: 5px;
}

input.error, textarea.error {
  border-color: #ff4d4f !important;
}

.success {
  color: #52c41a;
  padding: 10px;
  margin-bottom: 15px;
  background-color: #f6ffed;
  border: 1px solid #b7eb8f;
  border-radius: 4px;
}

.socials a {
  display: inline-block;
  margin-right: 15px;
  font-size: 22px;
  color: #666;
  transition: color 0.3s;
}

.socials a:hover {
  color: #ff5a00;
}

.btn {
  background: #ff5a00;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn:hover {
  background: #e04a00;
}
</style>
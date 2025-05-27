<template>
  <div class="setting-page">
    <el-row :gutter="24">
      <el-col :span="8">
        <el-card class="box-card avatar-card">
          <template #header>
            <div class="card-header">头像信息</div>
          </template>
          <div class="avatar-section">
            <el-avatar class="avatar" shape="square" :size="80" :src="avatarURL" />
            <el-upload
              ref="uploadRef"
              class="upload-demo"
              :limit="1"
              :action="uploadURL"
              :headers="headers"
              :data="uploadData"
              :auto-upload="false"
              :on-success="uploadSuccess">
              <template #trigger>
                <el-button type="primary" class="upload-btn">选择头像</el-button>
              </template>
              <el-button type="success" class="upload-btn" @click="submitUpload">上传头像</el-button>
              <template #tip>
                <div class="el-upload__tip"><p>限制上传1个文件，且旧文件会被新文件覆盖</p></div>
              </template>
            </el-upload>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="box-card info-card">
          <template #header>
            <span class="card-header">个人信息</span>
          </template>
          <div class="change-password-box">
            <el-form ref="ruleFormRef" status-icon :model="form" :rules="rules" label-width="120px" class="setting-form">
              <el-form-item prop="password" label="修改密码">
                <el-input type="password" v-model="form.password" clearable />
              </el-form-item>
              <el-form-item prop="password2" label="输入密码">
                <el-input type="password" v-model="form.password2" clearable />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm(ruleFormRef)">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>

import { ref, reactive } from 'vue'
import defaultAvatarURL from  '/images/avatar-default.png'
import { changeAdminPassword, changeAdminAvatar, uploadPictureURL } from '../../api'
import notification from '../../utils/notification'
import useToken from '../../stores/token'
import useAdmin from '../../stores/admin'
import router from '../../router'

const { admin, updateAdmin, removeAdmin  } = useAdmin()
const { token, removeToken } = useToken()
const headers = { jwt: token }

const uploadURL = uploadPictureURL()
const uploadData = { type: 'admin_avatar' }

const form = reactive({
  password: '',
  password2: ''
})
const avatarURL = ref(admin.avatar || defaultAvatarURL)
const ruleFormRef = ref()
const uploadRef = ref()

const submitForm = formEl => {
  formEl.validate(async valid => {
    if (valid) {
      await changeAdminPassword({ password: form.password })
      resetForm()
      // 退出登录
      removeToken()
      removeAdmin()
      router.push({ name: 'login' })
      notification({
        message: '修改密码后，请重新登录',
        type: 'warning'
      })
    } else {
      notification({
        message: '表单填写有误',
        type: 'error'
      })
    }
  })
}

const resetForm = () => {
  ruleFormRef.value.resetFields();
}

const submitUpload = () => {
  uploadRef.value.submit()
}

const uploadSuccess = async response => {
  const { errno, errmsg, data } = response
  if (errno !== 0) {
    notification({
      message: errmsg,
      type: 'error'
    })
  } else {
    if (errmsg !== '') {
      notification({
        message: errmsg,
        type: 'success'
      })
    }
    await changeAdminAvatar({
      avatar: data.savepath
    })
    updateAdmin({
      avatar: data.url
    })
    avatarURL.value = data.url
  }
  uploadRef.value.clearFiles()
}

const validatePass = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入密码不一致！'))
  } else {
    callback()
  }
}

const rules = reactive({
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 24, message: '密码长度为6~24个字符', trigger: 'blur' },
  ],
  password2: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ]
})

</script>

<style lang="scss" scoped>
.setting-page {
  min-height: 100vh;
}

.el-row {
  min-height: 340px;
}

.box-card {
  border-radius: 14px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.04);
  transition: box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
  &:hover {
    box-shadow: 0 4px 24px 0 rgba(0,0,0,0.08);
  }
  .card-header {
    font-size: 18px;
    font-weight: 600;
    color: #444;
    letter-spacing: 1px;
  }
}

.avatar-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  .avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 240px;
    .avatar {
      margin-bottom: 18px;
      border-radius: 10px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      border: 2px solid #e4e7ed;
    }
    .upload-demo {
      display: flex;
      flex-direction: column;
      align-items: center;
      .upload-btn {
        margin-bottom: 10px;
        width: 120px;
      }
      .el-upload__tip {
        color: #888;
        font-size: 13px;
        margin-top: 6px;
      }
    }
  }
}

.info-card {
  .change-password-box {
    padding-top: 18px;
    display: flex;
    flex-direction: column;
    align-items: center;
    .setting-form {
      width: 80%;
      min-width: 320px;
      max-width: 420px;
      margin: 0 auto;
      .el-form-item {
        margin-bottom: 22px;
      }
      .el-button {
        min-width: 90px;
      }
    }
  }
}
</style>
<template>
    <div class="photo-management">
        <h2>照片上传</h2>

        <!-- 照片上传表单 -->
        <div class="upload-section">
            <form @submit.prevent="uploadPhoto" class="upload-form">
                <div class="form-group">
                    <label for="category">分类:</label>
                    <select
                            id="category"
                            v-model="uploadForm.category"
                            required
                    >
                        <option value="">请选择分类</option>
                        <option value="婚纱照">婚纱照</option>
                        <option value="写真">写真</option>
                        <option value="旅拍">旅拍</option>
                        <option value="亲子">亲子</option>
                        <option value="幼童">幼童</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="photo">选择照片:</label>
                    <input
                            type="file"
                            id="photo"
                            ref="fileInput"
                            @change="handleFileChange"
                            accept="image/*"
                            required
                    />
                </div>

                <button type="submit" :disabled="uploading">
                    {{ uploading ? '上传中...' : '上传照片' }}
                </button>

                <div v-if="uploadError" class="error-message">
                    {{ uploadError }}
                </div>

                <div v-if="uploadSuccess" class="success-message">
                    照片上传成功！
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
    import { ref } from 'vue';
    import api from '@/api/auth';

    const fileInput = ref(null);
    const uploading = ref(false);
    const uploadError = ref(null);
    const uploadSuccess = ref(false);

    const uploadForm = ref({
        category: '',
        file: null
    });

    // 处理文件选择
    const handleFileChange = (e) => {
        const files = e.target.files;
        if (files && files.length > 0) {
            uploadForm.value.file = files[0];
        }
    };

    // 上传照片
    const uploadPhoto = async () => {
        if (!uploadForm.value.file || !uploadForm.value.category) {
            uploadError.value = '请选择照片并填写分类';
            return;
        }

        uploading.value = true;
        uploadError.value = null;
        uploadSuccess.value = false;

        const formData = new FormData();
        formData.append('file', uploadForm.value.file);
        formData.append('category', uploadForm.value.category);

        try {
            await api.post('/api/photos', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            uploadSuccess.value = true;
            uploadForm.value.category = '';
            if (fileInput.value) {
                fileInput.value.value = '';
            }

            setTimeout(() => {
                uploadSuccess.value = false;
            }, 3000);
        } catch (error) {
            console.error('上传照片失败:', error);
            uploadError.value = error.response?.data?.message || '上传照片失败';
        } finally {
            uploading.value = false;
        }
    };
</script>

<style scoped>
    .photo-management {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
    }

    .upload-section {
        background: #fff;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .upload-form {
        display: flex;
        flex-direction: column;
        gap: 15px;
    }

    .form-group {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }

    select, input[type="file"] {
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    button {
        background: #4CAF50;
        color: white;
        border: none;
        padding: 10px 15px;
        border-radius: 4px;
        cursor: pointer;
        transition: background 0.3s;
    }

    button:hover {
        background: #45a049;
    }

    button:disabled {
        background: #cccccc;
        cursor: not-allowed;
    }

    .error-message {
        color: #f44336;
        margin-top: 10px;
    }

    .success-message {
        color: #4CAF50;
        margin-top: 10px;
    }
</style>
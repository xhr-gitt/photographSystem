<template>
    <div class="comment-system">
        <!-- 评论表单 -->
        <router-link to="/main" class="text-xl font-semibold text-gray-900">
            返回首页
        </router-link>
        <div class="comment-form">
            <h3>发表评论</h3>

            <form @submit.prevent="submitComment">
        <textarea
                v-model="newComment.text"
                placeholder="请输入您的评论..."
                maxlength="1000"
        ></textarea>
                <div class="form-footer">
                    <span class="char-count">{{ newComment.text.length }}/1000</span>
                    <button type="submit" :disabled="!canSubmit">提交评论</button>
                </div>
            </form>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
            <h3>评论列表 ({{ comments.length }})</h3>
            <div v-if="loading" class="loading">加载中...</div>
            <div v-else-if="comments.length === 0" class="no-comments">
                暂无评论，快来发表第一条评论吧！
            </div>
            <div v-else>
                <div v-for="comment in comments" :key="comment.id" class="comment-item">
                    <div class="comment-header">
                        <span class="user">用户ID: {{ comment.userId }}</span>
                        <span class="date">{{ formatDate(comment.createdAt) }}</span>
                        <button
                                v-if="currentUserId === comment.userId"
                                @click="deleteComment(comment.id)"
                                class="delete-btn"
                        >
                            删除
                        </button>
                    </div>
                    <div class="comment-content">{{ comment.text }}</div>

                    <!-- 回复功能 (可选) -->
                    <button
                            v-if="!comment.parentId"
                            @click="showReplyForm(comment.id)"
                            class="reply-btn"
                    >
                        回复
                    </button>

                    <!-- 回复表单 (可选) -->
                    <div v-if="replyingTo === comment.id" class="reply-form">
            <textarea
                    v-model="replyComment.text"
                    placeholder="请输入回复内容..."
                    maxlength="1000"
            ></textarea>
                        <button @click="submitReply(comment.id)" :disabled="!replyComment.text.trim()">
                            提交回复
                        </button>
                        <button @click="cancelReply">取消</button>
                    </div>

                    <!-- 子评论 (回复) -->
                    <div v-if="comment.replies && comment.replies.length > 0" class="replies">
                        <div
                                v-for="reply in comment.replies"
                                :key="reply.id"
                                class="reply-item"
                        >
                            <div class="reply-header">
                                <span class="user">用户ID: {{ reply.userId }}</span>
                                <span class="date">{{ formatDate(reply.createdAt) }}</span>
                                <button
                                        v-if="currentUserId === reply.userId"
                                        @click="deleteComment(reply.id)"
                                        class="delete-btn"
                                >
                                    删除
                                </button>
                            </div>
                            <div class="reply-content">{{ reply.text }}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted, computed } from 'vue';
    import { useAuthStore } from '@/stores/auth';
    import api from '@/api/auth';

    const authStore = useAuthStore();
    const currentUserId = ref(authStore.userId); // 从store获取当前用户ID

    // 评论数据
    const comments = ref([]);
    const loading = ref(true);
    const error = ref(null);

    // 新评论表单
    const newComment = ref({
        text: '',
        userId: currentUserId.value,
        parentId: null
    });

    // 回复相关状态
    const replyingTo = ref(null);
    const replyComment = ref({
        text: '',
        userId: currentUserId.value,
        parentId: null
    });

    // 计算属性：是否可以提交评论
    const canSubmit = computed(() => {
        return newComment.value.text.trim().length > 0;
    });

    // 日期格式化
    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString();
    };

    // 获取所有评论
    const fetchComments = async () => {
        try {
            loading.value = true;
            const response = await api.get('/api/comments');
            comments.value = response.data;
        } catch (err) {
            console.error('获取评论失败:', err);
            error.value = '获取评论失败，请稍后再试';
        } finally {
            loading.value = false;
        }
    };

    // 提交新评论
    const submitComment = async () => {
        try {
            const response = await api.post('/api/comments', newComment.value);
            comments.value.unshift(response.data); // 添加到列表顶部
            newComment.value.text = ''; // 清空表单
        } catch (err) {
            console.error('提交评论失败:', err);
            alert('提交评论失败: ' + (err.response?.data?.message || err.message));
        }
    };

    // 删除评论
    const deleteComment = async (commentId) => {
        if (!confirm('确定要删除这条评论吗？')) return;

        try {
            const success = await api.delete(`/api/comments/${commentId}?userId=${currentUserId.value}`);
            if (success) {
                comments.value = comments.value.filter(c => c.id !== commentId);
                // 如果是回复，也需要从父评论的replies中移除
                comments.value.forEach(comment => {
                    if (comment.replies) {
                        comment.replies = comment.replies.filter(r => r.id !== commentId);
                    }
                });
            }
        } catch (err) {
            console.error('删除评论失败:', err);
            alert('删除评论失败: ' + (err.response?.data?.message || err.message));
        }
    };

    // 显示回复表单
    const showReplyForm = (commentId) => {
        replyingTo.value = commentId;
        replyComment.value = {
            text: '',
            userId: currentUserId.value,
            parentId: commentId
        };
    };

    // 取消回复
    const cancelReply = () => {
        replyingTo.value = null;
    };

    // 提交回复
    const submitReply = async (parentId) => {
        try {
            const response = await api.post('/api/comments', replyComment.value);

            // 找到父评论并添加回复
            const parentComment = comments.value.find(c => c.id === parentId);
            if (parentComment) {
                if (!parentComment.replies) {
                    parentComment.replies = [];
                }
                parentComment.replies.push(response.data);
            }

            replyingTo.value = null;
            replyComment.value.text = '';
        } catch (err) {
            console.error('提交回复失败:', err);
            alert('提交回复失败: ' + (err.response?.data?.message || err.message));
        }
    };

    // 初始化加载评论
    onMounted(() => {
        fetchComments();
    });
</script>

<style scoped>
    .comment-system {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
        font-family: Arial, sans-serif;
    }

    .comment-form, .comment-list {
        background: #fff;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    h3 {
        margin-top: 0;
        color: #333;
        border-bottom: 1px solid #eee;
        padding-bottom: 10px;
    }

    textarea {
        width: 100%;
        min-height: 100px;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        resize: vertical;
        font-family: inherit;
        margin-bottom: 10px;
    }

    .form-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .char-count {
        color: #999;
        font-size: 0.9em;
    }

    button {
        background: #4CAF50;
        color: white;
        border: none;
        padding: 8px 16px;
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

    .comment-item {
        border-bottom: 1px solid #eee;
        padding: 15px 0;
    }

    .comment-header, .reply-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 5px;
        font-size: 0.9em;
        color: #666;
    }

    .comment-content, .reply-content {
        margin: 10px 0;
        line-height: 1.5;
    }

    .delete-btn {
        background: #f44336;
        padding: 4px 8px;
        font-size: 0.8em;
    }

    .delete-btn:hover {
        background: #d32f2f;
    }

    .reply-btn {
        background: #2196F3;
        padding: 4px 8px;
        font-size: 0.8em;
        margin-top: 5px;
    }

    .reply-btn:hover {
        background: #0b7dda;
    }

    .reply-form {
        margin-top: 10px;
        padding-left: 20px;
        border-left: 2px solid #eee;
    }

    .replies {
        margin-top: 10px;
        padding-left: 20px;
        border-left: 2px solid #eee;
    }

    .reply-item {
        margin: 10px 0;
        padding: 10px;
        background: #f9f9f9;
        border-radius: 4px;
    }

    .loading, .no-comments {
        text-align: center;
        padding: 20px;
        color: #666;
    }
</style>
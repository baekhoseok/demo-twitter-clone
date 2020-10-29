import produce from 'immer';

export const initialState = {
  posts: [],
  imagePaths: [],
  hasMorePost: true,
  loadPostsLoading: false,
  loadPostsDone: false,
  loadPostsError: null,
  loadPostLoading: false,
  loadPostDone: false,
  loadPostError: null,
  likePostsLoading: false,
  likePostsDone: false,
  likePostsError: null,
  unLikePostsLoading: false,
  unLikePostsDone: false,
  unLikePostsError: null,
  addPostLoading: false,
  addPostDone: false,
  addPostError: null,
  updatePostLoading: false,
  updatePostDone: false,
  updatePostError: null,
  removePostLoading: false,
  removePostDone: false,
  removePostError: null,
  retweetPostLoading: false,
  retweetPostDone: false,
  retweetPostError: null,
  addCommentLoading: false,
  addCommentDone: false,
  addCommentError: null,
  uploadImagesLoading: false,
  uploadImagesDone: false,
  uploadImagesError: null,
  singlePost: null,
  pageIndex: 0,

};

export const LOAD_POSTS_REQ = 'LOAD_POSTS_REQ';
export const LOAD_POSTS_SUCCESS = 'LOAD_POSTS_SUCCESS';
export const LOAD_POSTS_FAILURE = 'LOAD_POSTS_FAILURE';
export const LOAD_USER_POSTS_REQ = 'LOAD_USER_POSTS_REQ';
export const LOAD_USER_POSTS_SUCCESS = 'LOAD_USER_POSTS_SUCCESS';
export const LOAD_USER_POSTS_FAILURE = 'LOAD_USER_POSTS_FAILURE';
export const LOAD_HASHTAG_POSTS_REQ = 'LOAD_HASHTAG_POSTS_REQ';
export const LOAD_HASHTAG_POSTS_SUCCESS = 'LOAD_HASHTAG_POSTS_SUCCESS';
export const LOAD_HASHTAG_POSTS_FAILURE = 'LOAD_HASHTAG_POSTS_FAILURE';
export const LOAD_POST_REQ = 'LOAD_POST_REQ';
export const LOAD_POST_SUCCESS = 'LOAD_POST_SUCCESS';
export const LOAD_POST_FAILURE = 'LOAD_POST_FAILURE';
export const ADD_POST_REQ = 'ADD_POST_REQ';
export const ADD_POST_SUCCESS = 'ADD_POST_SUCCESS';
export const ADD_POST_FAILURE = 'ADD_POST_FAILURE';
export const UPDATE_POST_REQ = 'UPDATE_POST_REQ';
export const UPDATE_POST_SUCCESS = 'UPDATE_POST_SUCCESS';
export const UPDATE_POST_FAILURE = 'UPDATE_POST_FAILURE';
export const REMOVE_POST_REQ = 'REMOVE_POST_REQ';
export const REMOVE_POST_SUCCESS = 'REMOVE_POST_SUCCESS';
export const REMOVE_POST_FAILURE = 'REMOVE_POST_FAILURE';
export const RETWEET_POST_REQ = 'RETWEET_POST_REQ';
export const RETWEET_POST_SUCCESS = 'RETWEET_POST_SUCCESS';
export const RETWEET_POST_FAILURE = 'RETWEET_POST_FAILURE';

export const LIKE_POST_REQ = 'LIKE_POST_REQ';
export const LIKE_POST_SUCCESS = 'LIKE_POST_SUCCESS';
export const LIKE_POST_FAILURE = 'LIKE_POST_FAILURE';
export const UNLIKE_POST_REQ = 'UNLIKE_POST_REQ';
export const UNLIKE_POST_SUCCESS = 'UNLIKE_POST_SUCCESS';
export const UNLIKE_POST_FAILURE = 'UNLIKE_POST_FAILURE';
export const UPLOAD_IMAGES_REQ = 'UPLOAD_IMAGES_REQ';
export const UPLOAD_IMAGES_SUCCESS = 'UPLOAD_IMAGES_SUCCESS';
export const UPLOAD_IMAGES_FAILURE = 'UPLOAD_IMAGES_FAILURE';

export const ADD_COMMENT_REQ = 'ADD_COMMENT_REQ';
export const ADD_COMMENT_SUCCESS = 'ADD_COMMENT_SUCCESS';
export const ADD_COMMENT_FAILURE = 'ADD_COMMENT_FAILURE';

export const REMOVE_IMAGE = 'REMOVE_IMAGE';
export const ADD_POST_TO_ME = 'ADD_POST_TO_ME';
export const REMOVE_POST_OF_ME = 'REMOVE_POST_OF_ME';

export const addPost = (data) => ({
  type: ADD_POST_REQ,
  data,
});

export const addComment = (data) => ({
  type: ADD_COMMENT_REQ,
  data,
});

const reducer = (state = initialState, action) => produce(state, (draft) => {
  switch (action.type) {
    case UPLOAD_IMAGES_REQ:
      draft.uploadImagesLoading = true;
      draft.uploadImagesDone = false;
      draft.uploadImagesError = null;
      break;
    case UPLOAD_IMAGES_SUCCESS:
      draft.imagePaths = action.data;
      draft.uploadImagesLoading = false;
      draft.uploadImagesDone = true;
      break;
    case UPLOAD_IMAGES_FAILURE:
      draft.uploadImagesLoading = false;
      draft.uploadImagesError = action.error;
      break;

    case LOAD_POSTS_REQ:
    case LOAD_USER_POSTS_REQ:
    case LOAD_HASHTAG_POSTS_REQ:
      draft.loadPostsLoading = true;
      draft.loadPostsDone = false;
      draft.loadPostsError = null;
      break;
    case LOAD_POSTS_SUCCESS:
    case LOAD_USER_POSTS_SUCCESS:
    case LOAD_HASHTAG_POSTS_SUCCESS:
      draft.posts = draft.posts.concat(action.data.content);
      draft.loadPostsLoading = false;
      draft.loadPostsDone = true;
      draft.pageIndex = action.data.number;
      draft.hasMorePost = !action.data.last;
      break;
    case LOAD_POSTS_FAILURE:
    case LOAD_USER_POSTS_FAILURE:
    case LOAD_HASHTAG_POSTS_FAILURE:
      draft.loadPostsLoading = false;
      draft.loadPostsError = action.error;
      break;

    case LOAD_POST_REQ:
      draft.loadPostLoading = true;
      draft.loadPostDone = false;
      draft.loadPostError = null;
      break;
    case LOAD_POST_SUCCESS:
      draft.singlePost = action.data;
      draft.loadPostLoading = false;
      draft.loadPostDone = true;
      break;
    case LOAD_POST_FAILURE:
      draft.loadPostLoading = false;
      draft.loadPostError = action.error;
      break;
    case ADD_POST_REQ:
      draft.addPostLoading = true;
      draft.addPostDone = false;
      draft.addPostError = null;
      break;
    case ADD_POST_SUCCESS:
      draft.posts.unshift(action.data);
      draft.addPostLoading = false;
      draft.addPostDone = true;
      draft.imagePaths = [];
      break;
    case ADD_POST_FAILURE:
      draft.addPostLoading = false;
      draft.addPostError = action.error;
      break;
    case UPDATE_POST_REQ:
      draft.updatePostLoading = true;
      draft.updatePostDone = false;
      draft.updatePostError = null;
      break;
    case UPDATE_POST_SUCCESS:
      draft.updatePostLoading = false;
      draft.updatePostDone = true;
      draft.posts.find((v) => v.id === action.data.id).content = action.data.content;
      break;
    case UPDATE_POST_FAILURE:
      draft.updatePostLoading = false;
      draft.updatePostError = action.error;
      break;
    case RETWEET_POST_REQ:
      draft.retweetPostLoading = true;
      draft.retweetPostDone = false;
      draft.retweetPostError = null;
      break;
    case RETWEET_POST_SUCCESS:
      draft.posts.unshift(action.data);
      draft.retweetPostLoading = false;
      draft.retweetPostDone = true;
      draft.imagePaths = [];
      break;
    case RETWEET_POST_FAILURE:
      draft.retweetPostLoading = false;
      draft.retweetPostError = action.error;
      break;

    case LIKE_POST_REQ:
      draft.likePostLoading = true;
      draft.likePostDone = false;
      draft.likePostError = null;
      break;
    case LIKE_POST_SUCCESS: {
      const post = draft.posts.find((v) => v.id === action.data.postId);
      post.likes.push({ accountId: action.data.accountId });
      draft.likePostLoading = false;
      draft.likePostDone = true;
      break;
    }
    case LIKE_POST_FAILURE:
      draft.likePostLoading = false;
      draft.likePostError = action.error;
      break;
    case UNLIKE_POST_REQ:
      draft.unLikePostLoading = true;
      draft.unLikePostDone = false;
      draft.unLikePostError = null;
      break;
    case UNLIKE_POST_SUCCESS: {
      const post = draft.posts.find((v) => v.id === action.data.postId);
      post.likes = post.likes.filter((v) => v.accountId !== action.data.accountId);
      draft.unLikePostLoading = false;
      draft.unLikePostDone = true;
      break;
    }
    case UNLIKE_POST_FAILURE:
      draft.unLikePostLoading = false;
      draft.unLikePostError = action.error;
      break;
    case REMOVE_POST_REQ:
      draft.removePostLoading = true;
      draft.removePostDone = false;
      draft.removePostError = null;
      break;
    case REMOVE_POST_SUCCESS:
      draft.posts = draft.posts.filter((v) => v.id !== action.data.id);
      draft.removePostLoading = false;
      draft.removePostDone = true;
      break;
    case REMOVE_POST_FAILURE:
      draft.removePostLoading = false;
      draft.removePostError = action.error;
      break;
    case ADD_COMMENT_REQ:
      draft.addCommentLoading = true;
      draft.addCommentDone = false;
      draft.addCommentError = null;
      break;
    case ADD_COMMENT_SUCCESS:
      const post = draft.posts.find((v) => v.id === action.data.postId);
      post.comments.unshift(action.data);
      draft.addCommentLoading = false;
      draft.addCommentDone = true;
      break;
    case ADD_COMMENT_FAILURE:
      draft.addCommentLoading = false;
      draft.addCommentError = action.error;
      break;
    case REMOVE_IMAGE:
      const index = draft.imagePaths.findIndex((v, i) => i === action.index);
      draft.imagePaths.splice(index, 1);
      break;
    default:
      return state;
  }
});

export default reducer;

import shortId from 'shortid';
import produce from 'immer';
import faker from 'faker';

export const initialState = {
  posts: [],
  imagePaths: [],
  hasMorePost: true,
  loadPostsLoading: false,
  loadPostsDone: false,
  loadPostsError: null,
  likePostsLoading: false,
  likePostsDone: false,
  likePostsError: null,
  unLikePostsLoading: false,
  unLikePostsDone: false,
  unLikePostsError: null,
  addPostLoading: false,
  addPostDone: false,
  addPostError: null,
  removePostLoading: false,
  removePostDone: false,
  removePostError: null,
  addCommentLoading: false,
  addCommentDone: false,
  addCommentError: null,
  pageIndex: 0,

};

export const LOAD_POSTS_REQ = 'LOAD_POSTS_REQ';
export const LOAD_POSTS_SUCCESS = 'LOAD_POSTS_SUCCESS';
export const LOAD_POSTS_FAILURE = 'LOAD_POSTS_FAILURE';
export const ADD_POST_REQ = 'ADD_POST_REQ';
export const ADD_POST_SUCCESS = 'ADD_POST_SUCCESS';
export const ADD_POST_FAILURE = 'ADD_POST_FAILURE';
export const REMOVE_POST_REQ = 'REMOVE_POST_REQ';
export const REMOVE_POST_SUCCESS = 'REMOVE_POST_SUCCESS';
export const REMOVE_POST_FAILURE = 'REMOVE_POST_FAILURE';

export const LIKE_POST_REQ = 'LIKE_POST_REQ';
export const LIKE_POST_SUCCESS = 'LIKE_POST_SUCCESS';
export const LIKE_POST_FAILURE = 'LIKE_POST_FAILURE';
export const UNLIKE_POST_REQ = 'UNLIKE_POST_REQ';
export const UNLIKE_POST_SUCCESS = 'UNLIKE_POST_SUCCESS';
export const UNLIKE_POST_FAILURE = 'UNLIKE_POST_FAILURE';

export const ADD_COMMENT_REQ = 'ADD_COMMENT_REQ';
export const ADD_COMMENT_SUCCESS = 'ADD_COMMENT_SUCCESS';
export const ADD_COMMENT_FAILURE = 'ADD_COMMENT_FAILURE';

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
    case LOAD_POSTS_REQ:
      draft.loadPostsLoading = true;
      draft.loadPostsDone = false;
      draft.loadPostsError = null;
      break;
    case LOAD_POSTS_SUCCESS:
      draft.posts = draft.posts.concat(action.data.content);
      draft.loadPostsLoading = false;
      draft.loadPostsDone = true;
      draft.pageIndex = action.data.number;
      draft.hasMorePost = !action.data.last;
      break;
    case LOAD_POSTS_FAILURE:
      draft.loadPostsLoading = false;
      draft.loadPostsError = action.error;
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
      break;
    case ADD_POST_FAILURE:
      draft.addPostLoading = false;
      draft.addPostError = action.error;
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
      console.log(action);
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
    default:
      return state;
  }
});

export default reducer;

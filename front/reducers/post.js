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
  addPostLoading: false,
  addPostDone: false,
  addPostError: null,
  removePostLoading: false,
  removePostDone: false,
  removePostError: null,
  addCommentLoading: false,
  addCommentDone: false,
  addCommentError: null,
};

export const generateDummyPosts = (number) => Array(number).fill().map(() => ({
  id: shortId.generate(),
  User: {
    id: shortId.generate(),
    nickname: faker.name.findName(),
  },
  content: faker.lorem.paragraph(),
  Images: [{
    src: faker.image.image(),
  }, {
    src: faker.image.image(),
  }, {
    src: faker.image.image(),
  }],
  Comments: [{
    User: {
      id: shortId.generate(),
      username: faker.name.findName(),
    },
    content: faker.lorem.sentence(),
  }, {
    User: {
      id: shortId.generate(),
      username: faker.name.findName(),
    },
    content: faker.lorem.sentence(),
  }, {
    User: {
      id: shortId.generate(),
      username: faker.name.findName(),
    },
    content: faker.lorem.sentence(),
  }],

}));

export const LOAD_POSTS_REQ = 'LOAD_POSTS_REQ';
export const LOAD_POSTS_SUCCESS = 'LOAD_POSTS_SUCCESS';
export const LOAD_POSTS_FAILURE = 'LOAD_POSTS_FAILURE';
export const ADD_POST_REQ = 'ADD_POST_REQ';
export const ADD_POST_SUCCESS = 'ADD_POST_SUCCESS';
export const ADD_POST_FAILURE = 'ADD_POST_FAILURE';
export const REMOVE_POST_REQ = 'REMOVE_POST_REQ';
export const REMOVE_POST_SUCCESS = 'REMOVE_POST_SUCCESS';
export const REMOVE_POST_FAILURE = 'REMOVE_POST_FAILURE';

export const ADD_COMMENT_REQ = 'ADD_COMMENT_REQ';
export const ADD_COMMENT_SUCCESS = 'ADD_COMMENT_SUCCESS';
export const ADD_COMMENT_FAILURE = 'ADD_COMMENT_FAILURE';

export const ADD_POST_TO_ME = 'ADD_POST_TO_ME';
export const REMOVE_POST_OF_ME = 'REMOVE_POST_OF_ME';

const dummyPost = (data) => ({
  id: shortId.generate(),
  content: data,
  User: {
    id: 3,
    nickname: 'timothy',
  },
  Images: [],
  Comments: [],
});

const dummyComment = (data) => ({
  id: shortid.generate(),
  content: data,
  User: {
    id: 1,
    nickname: 'hoseok',
  },

});

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
      draft.posts = action.data.concat(draft.posts);
      draft.loadPostsLoading = false;
      draft.loadPostsDone = true;
      draft.hasMorePost = draft.posts.length < 50;
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
      draft.posts.unshift(dummyPost(action.data));
      draft.addPostLoading = false;
      draft.addPostDone = true;
      break;
    case ADD_POST_FAILURE:
      draft.addPostLoading = false;
      draft.addPostError = action.error;
      break;
    case REMOVE_POST_REQ:
      draft.removePostLoading = true;
      draft.removePostDone = false;
      draft.removePostError = null;
      break;
    case REMOVE_POST_SUCCESS:
      draft.posts = state.posts.filter((v) => v.id !== action.data);
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
      post.Comments.unshift(dummyComment(action.data.content));
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

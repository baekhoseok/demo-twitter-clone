import PropTypes from "prop-types";
import "antd/dist/antd.css";
import Head from "next/head";
import withReduxSaga from "next-redux-saga";

import wrapper from "../store/configureStore";

const App = ({ Component }) => {
  return (
    <>
      <Head>
        <meta charSet="utf-8" />
        <title>Twitter Clone</title>
      </Head>
      <Component />
    </>
  );
};

App.propTypes = {
  Component: PropTypes.elementType.isRequired
};

export default wrapper.withRedux(withReduxSaga(App));

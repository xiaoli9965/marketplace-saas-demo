import React, {useEffect, useState} from "react";

import {PageContainer} from "@ant-design/pro-layout";
import {ProFormField} from "@ant-design/pro-form";
import {authUrlLong, requestQingcCloudSso} from "@/services/apis/sso-api";
import {Alert, Card, message, Space} from "antd";
import {getApp} from "@/services/apis/third-app-api";
import logo from "@@/plugin-layout/layout/component/logo";
import {json} from "express";
import {login} from "@/services/ant-design-pro/api";
import {setAuthority, setSessionInstance} from "@/utils/authority";
import {history} from "@@/core/history";
import {useModel} from "@@/plugin-model/useModel";

type QcTokenRet = {
  ret_code: number;
  message: string
  access_token: string
}
type QcCkTokenRet = {
  ret_code: number;
  message: string
  user: any
}

/** 此方法会跳转到 redirect 参数所在的位置 */
const goto = () => {
  if (!history) return;
  setTimeout(() => {
    const {query} = history.location;
    const {redirect} = query as { redirect: string };
    history.push(redirect || '/');
  }, 10);
};

const SsoAuthUrl: React.FC = (props) => {
  const queryParams = props.location.query
  const [paramOoK, setParamOk] = useState<boolean>(!(Object.keys(queryParams).length === 0));
  const [errMsg, setErrmsg] = useState<string>();
  const [okMsg, setOkmsg] = useState<string>("参数校验中...");

  const [tokenRet, setTokenRet] = useState<QcTokenRet>();
  const [ckTokenRet, setCkTokenRet] = useState<QcCkTokenRet>();

  const {initialState, setInitialState} = useModel('@@initialState');

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      setInitialState({
        ...initialState,
        currentUser: userInfo,
      });
    }
  };

  useEffect(async () => {
    if (!paramOoK) {
      setErrmsg("无效的跳转链接")
      return
    }

    const resp = await getApp(queryParams.isv_ins_id);
    if (resp.code !== 200) {
      setErrmsg(resp.message)
      setParamOk(false)
      return
    }
    const {app: aInfo, instance: iInfo} = resp.data
    const cloudInfo = JSON.parse(iInfo.cloudInfo)
    const opt = {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      requestType: 'form',
      data: {
        "grant_type": "authorization_code",
        "code": queryParams.code,
        "client_id": aInfo.appId,
        "client_secret": aInfo.ssoKey,
        "token_issuer": "sso",
      }
    }

    setOkmsg("请求token中...")
    // eslint-disable-next-line @typescript-eslint/no-shadow
    const qcToken: QcTokenRet = await requestQingcCloudSso(cloudInfo.sso_server + '/sso/token/', opt)
    setTokenRet(qcToken)
    if (qcToken.ret_code !== 0) {
      setErrmsg("请求token失败")
      setParamOk(false)
      return
    }
    const ckTokenReq = {
      headers: {
        "Authorization": `Bearer ${qcToken.access_token}`,
      },
    }
    setOkmsg("验证token中...")
    const qcCkTokenRet: QcCkTokenRet = await requestQingcCloudSso(cloudInfo.sso_server + '/sso/check_token/', ckTokenReq)
    setCkTokenRet(qcCkTokenRet)
    if (qcCkTokenRet.ret_code !== 0) {
      setErrmsg("验证token失败")
      setParamOk(false)
      return
    }
    if (qcCkTokenRet?.user?.user_id !== iInfo.userId) {
      setErrmsg("用户验证失败")
      setParamOk(false)
      return
    }
    setOkmsg("登录成功,即将跳转...")
    message.success('登录成功2秒后跳转');
    setTimeout(async () => {
      setAuthority(iInfo.userId)
      setSessionInstance(queryParams.isv_ins_id)
      await fetchUserInfo();
      goto()
    }, 2000)


  }, [])

  const noneEl = () => <Alert
    message="免密登录失败"
    description={errMsg}
    type="error"
    showIcon
  />

  return (
    <div>
      <div className="site-card-wrapper">
        {!!errMsg ? noneEl() : (<Alert message={okMsg} type="info"/>)}
      </div>
      <br/>
      <br/>
      <Space wrap>
        <Card size="small" title="请求参数">
          <ProFormField width={400} ignoreFormItem valueType="jsonCode" text={JSON.stringify(queryParams)} mode="read"/>
        </Card>
        {
          !tokenRet ? undefined :
            <Card size="small" title="请求青云token响应">
              <ProFormField ignoreFormItem valueType="jsonCode" text={JSON.stringify(tokenRet)} mode="read"/>
            </Card>
        }
        {
          !ckTokenRet ? undefined :
            <Card size="small" title="青云验证token响应">
              <ProFormField ignoreFormItem valueType="jsonCode" text={JSON.stringify(ckTokenRet)} mode="read"/>
            </Card>
        }
      </Space>
    </div>
  )
}

export default SsoAuthUrl

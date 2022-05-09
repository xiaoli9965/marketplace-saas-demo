const TOKEN_NAME = "SAAS-DEMO"
const SAAS_INSTANCE = "SAAS-INSTANCE"

export function setAuthority(payload: string) {
  sessionStorage.setItem(TOKEN_NAME, payload);
}

export function getAuthority() {
  return sessionStorage.getItem(TOKEN_NAME);
}

export function clearAuthority() {
  return sessionStorage.removeItem(TOKEN_NAME);
}


export function setSessionInstance(payload: string) {
  sessionStorage.setItem(SAAS_INSTANCE, payload);
}

export function getSessionInstance() {
  return sessionStorage.getItem(SAAS_INSTANCE);
}

export function clearSessionInstance() {
  return sessionStorage.removeItem(SAAS_INSTANCE);
}



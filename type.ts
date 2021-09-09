export enum DeviceType {
  JPUSH_IOS = "JPUSH_IOS",
  JPUSHAndroid = "JPUSH_Android",
  XiaoMi = "XiaoMi",
  OPPO = "OPPO",
  VIVO = "VIVO",
  HuaWei = "HuaWei",
  MeiZu = "MeiZu",
}

export interface RNCRegisterRes {
  type: DeviceType,
  registrationId: string,
}

export interface RNCNotification{
  id:string,
  title:string,
  content:string,
  extras?:any,
}
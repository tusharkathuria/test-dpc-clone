/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.google.android.setupcompat.portal;
/** Listener to listen the result of registerProgressService */
public interface IPortalRegisterResultListener extends android.os.IInterface
{
  /** Default implementation for IPortalRegisterResultListener. */
  public static class Default implements com.google.android.setupcompat.portal.IPortalRegisterResultListener
  {
    @Override public void onResult(android.os.Bundle result) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.google.android.setupcompat.portal.IPortalRegisterResultListener
  {
    private static final java.lang.String DESCRIPTOR = "com.google.android.setupcompat.portal.IPortalRegisterResultListener";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.google.android.setupcompat.portal.IPortalRegisterResultListener interface,
     * generating a proxy if needed.
     */
    public static com.google.android.setupcompat.portal.IPortalRegisterResultListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.google.android.setupcompat.portal.IPortalRegisterResultListener))) {
        return ((com.google.android.setupcompat.portal.IPortalRegisterResultListener)iin);
      }
      return new com.google.android.setupcompat.portal.IPortalRegisterResultListener.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_onResult:
        {
          data.enforceInterface(descriptor);
          android.os.Bundle _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onResult(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.google.android.setupcompat.portal.IPortalRegisterResultListener
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void onResult(android.os.Bundle result) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((result!=null)) {
            _data.writeInt(1);
            result.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onResult, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onResult(result);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.google.android.setupcompat.portal.IPortalRegisterResultListener sDefaultImpl;
    }
    static final int TRANSACTION_onResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(com.google.android.setupcompat.portal.IPortalRegisterResultListener impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Stub.Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.google.android.setupcompat.portal.IPortalRegisterResultListener getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void onResult(android.os.Bundle result) throws android.os.RemoteException;
}

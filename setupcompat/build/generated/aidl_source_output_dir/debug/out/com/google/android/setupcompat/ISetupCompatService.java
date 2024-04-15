/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.google.android.setupcompat;
/**
 * Declares the interface for compat related service methods.
 */
public interface ISetupCompatService extends android.os.IInterface
{
  /** Default implementation for ISetupCompatService. */
  public static class Default implements com.google.android.setupcompat.ISetupCompatService
  {
    /** Notifies SetupWizard that the screen is using PartnerCustomizationLayout */
    @Override public void validateActivity(java.lang.String screenName, android.os.Bundle arguments) throws android.os.RemoteException
    {
    }
    @Override public void logMetric(int metricType, android.os.Bundle arguments, android.os.Bundle extras) throws android.os.RemoteException
    {
    }
    @Override public void onFocusStatusChanged(android.os.Bundle bundle) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.google.android.setupcompat.ISetupCompatService
  {
    private static final java.lang.String DESCRIPTOR = "com.google.android.setupcompat.ISetupCompatService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.google.android.setupcompat.ISetupCompatService interface,
     * generating a proxy if needed.
     */
    public static com.google.android.setupcompat.ISetupCompatService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.google.android.setupcompat.ISetupCompatService))) {
        return ((com.google.android.setupcompat.ISetupCompatService)iin);
      }
      return new com.google.android.setupcompat.ISetupCompatService.Stub.Proxy(obj);
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
        case TRANSACTION_validateActivity:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.Bundle _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          this.validateActivity(_arg0, _arg1);
          return true;
        }
        case TRANSACTION_logMetric:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          android.os.Bundle _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          android.os.Bundle _arg2;
          if ((0!=data.readInt())) {
            _arg2 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg2 = null;
          }
          this.logMetric(_arg0, _arg1, _arg2);
          return true;
        }
        case TRANSACTION_onFocusStatusChanged:
        {
          data.enforceInterface(descriptor);
          android.os.Bundle _arg0;
          if ((0!=data.readInt())) {
            _arg0 = android.os.Bundle.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.onFocusStatusChanged(_arg0);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.google.android.setupcompat.ISetupCompatService
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
      /** Notifies SetupWizard that the screen is using PartnerCustomizationLayout */
      @Override public void validateActivity(java.lang.String screenName, android.os.Bundle arguments) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(screenName);
          if ((arguments!=null)) {
            _data.writeInt(1);
            arguments.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_validateActivity, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().validateActivity(screenName, arguments);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void logMetric(int metricType, android.os.Bundle arguments, android.os.Bundle extras) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(metricType);
          if ((arguments!=null)) {
            _data.writeInt(1);
            arguments.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          if ((extras!=null)) {
            _data.writeInt(1);
            extras.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_logMetric, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().logMetric(metricType, arguments, extras);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onFocusStatusChanged(android.os.Bundle bundle) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((bundle!=null)) {
            _data.writeInt(1);
            bundle.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFocusStatusChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().onFocusStatusChanged(bundle);
            return;
          }
        }
        finally {
          _data.recycle();
        }
      }
      public static com.google.android.setupcompat.ISetupCompatService sDefaultImpl;
    }
    static final int TRANSACTION_validateActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_logMetric = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onFocusStatusChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    public static boolean setDefaultImpl(com.google.android.setupcompat.ISetupCompatService impl) {
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
    public static com.google.android.setupcompat.ISetupCompatService getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  /** Notifies SetupWizard that the screen is using PartnerCustomizationLayout */
  public void validateActivity(java.lang.String screenName, android.os.Bundle arguments) throws android.os.RemoteException;
  public void logMetric(int metricType, android.os.Bundle arguments, android.os.Bundle extras) throws android.os.RemoteException;
  public void onFocusStatusChanged(android.os.Bundle bundle) throws android.os.RemoteException;
}

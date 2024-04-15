/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.google.android.setupcompat.portal.v1_1;
/**
 * Interface for progress service to update progress to SUW. Clients should
 * update progress at least once a minute, or set a pending reason to stop
 * update progress. Without progress update and pending reason. We considering
 * the progress service is no response will suspend it and unbinde service.
 */
public interface IPortalProgressCallback extends android.os.IInterface
{
  /** Default implementation for IPortalProgressCallback. */
  public static class Default implements com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback
  {
    /**
       * Sets completed amount.
       */
    @Override public android.os.Bundle setProgressCount(int completed, int failed, int total) throws android.os.RemoteException
    {
      return null;
    }
    /**
       * Sets completed percentage.
       */
    @Override public android.os.Bundle setProgressPercentage(int percentage) throws android.os.RemoteException
    {
      return null;
    }
    /**
       * Sets the summary shows on portal activity.
       */
    @Override public android.os.Bundle setSummary(java.lang.String summary) throws android.os.RemoteException
    {
      return null;
    }
    /**
       * Let SUW knows the progress is pending now, and stop update progress.
       * @param reasonResName The name of resource identifier.
       * @param quantity The number used to get the correct string for the current language's
       *           plural rules
       * @param formatArgs The format arguments that will be used for substitution.
       */
    @Override public android.os.Bundle setPendingReason(java.lang.String reasonResName, int quantity, int[] formatArgs, int reason) throws android.os.RemoteException
    {
      return null;
    }
    /**
       * Once the progress completed, and service can be destroy. Call this function.
       * SUW will unbind the service.
       * @param resName The name of resource identifier.
       * @param quantity The number used to get the correct string for the current language's
       *           plural rules
       * @param formatArgs The format arguments that will be used for substitution.
       */
    @Override public android.os.Bundle setComplete(java.lang.String resName, int quantity, int[] formatArgs) throws android.os.RemoteException
    {
      return null;
    }
    /**
       * Once the progress failed, and not able to finish the progress. Should call
       * this function. SUW will unbind service after receive setFailure. Client can
       * registerProgressService again once the service is ready to execute.
       * @param resName The name of resource identifier.
       * @param quantity The number used to get the correct string for the current language's
       *           plural rules
       * @param formatArgs The format arguments that will be used for substitution.
       */
    @Override public android.os.Bundle setFailure(java.lang.String resName, int quantity, int[] formatArgs) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback
  {
    private static final java.lang.String DESCRIPTOR = "com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback interface,
     * generating a proxy if needed.
     */
    public static com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback))) {
        return ((com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback)iin);
      }
      return new com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback.Stub.Proxy(obj);
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
        case TRANSACTION_setProgressCount:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.os.Bundle _result = this.setProgressCount(_arg0, _arg1, _arg2);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_setProgressPercentage:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          android.os.Bundle _result = this.setProgressPercentage(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_setSummary:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.Bundle _result = this.setSummary(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_setPendingReason:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int[] _arg2;
          _arg2 = data.createIntArray();
          int _arg3;
          _arg3 = data.readInt();
          android.os.Bundle _result = this.setPendingReason(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_setComplete:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int[] _arg2;
          _arg2 = data.createIntArray();
          android.os.Bundle _result = this.setComplete(_arg0, _arg1, _arg2);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_setFailure:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int[] _arg2;
          _arg2 = data.createIntArray();
          android.os.Bundle _result = this.setFailure(_arg0, _arg1, _arg2);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback
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
      /**
         * Sets completed amount.
         */
      @Override public android.os.Bundle setProgressCount(int completed, int failed, int total) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.Bundle _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(completed);
          _data.writeInt(failed);
          _data.writeInt(total);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProgressCount, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setProgressCount(completed, failed, total);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.os.Bundle.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
         * Sets completed percentage.
         */
      @Override public android.os.Bundle setProgressPercentage(int percentage) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.Bundle _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(percentage);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProgressPercentage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setProgressPercentage(percentage);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.os.Bundle.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
         * Sets the summary shows on portal activity.
         */
      @Override public android.os.Bundle setSummary(java.lang.String summary) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.Bundle _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(summary);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setSummary, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setSummary(summary);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.os.Bundle.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
         * Let SUW knows the progress is pending now, and stop update progress.
         * @param reasonResName The name of resource identifier.
         * @param quantity The number used to get the correct string for the current language's
         *           plural rules
         * @param formatArgs The format arguments that will be used for substitution.
         */
      @Override public android.os.Bundle setPendingReason(java.lang.String reasonResName, int quantity, int[] formatArgs, int reason) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.Bundle _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(reasonResName);
          _data.writeInt(quantity);
          _data.writeIntArray(formatArgs);
          _data.writeInt(reason);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPendingReason, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setPendingReason(reasonResName, quantity, formatArgs, reason);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.os.Bundle.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
         * Once the progress completed, and service can be destroy. Call this function.
         * SUW will unbind the service.
         * @param resName The name of resource identifier.
         * @param quantity The number used to get the correct string for the current language's
         *           plural rules
         * @param formatArgs The format arguments that will be used for substitution.
         */
      @Override public android.os.Bundle setComplete(java.lang.String resName, int quantity, int[] formatArgs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.Bundle _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(resName);
          _data.writeInt(quantity);
          _data.writeIntArray(formatArgs);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setComplete, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setComplete(resName, quantity, formatArgs);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.os.Bundle.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
         * Once the progress failed, and not able to finish the progress. Should call
         * this function. SUW will unbind service after receive setFailure. Client can
         * registerProgressService again once the service is ready to execute.
         * @param resName The name of resource identifier.
         * @param quantity The number used to get the correct string for the current language's
         *           plural rules
         * @param formatArgs The format arguments that will be used for substitution.
         */
      @Override public android.os.Bundle setFailure(java.lang.String resName, int quantity, int[] formatArgs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.Bundle _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(resName);
          _data.writeInt(quantity);
          _data.writeIntArray(formatArgs);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setFailure, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setFailure(resName, quantity, formatArgs);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = android.os.Bundle.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback sDefaultImpl;
    }
    static final int TRANSACTION_setProgressCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_setProgressPercentage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_setSummary = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_setPendingReason = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_setFailure = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    public static boolean setDefaultImpl(com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback impl) {
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
    public static com.google.android.setupcompat.portal.v1_1.IPortalProgressCallback getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  /**
     * Sets completed amount.
     */
  public android.os.Bundle setProgressCount(int completed, int failed, int total) throws android.os.RemoteException;
  /**
     * Sets completed percentage.
     */
  public android.os.Bundle setProgressPercentage(int percentage) throws android.os.RemoteException;
  /**
     * Sets the summary shows on portal activity.
     */
  public android.os.Bundle setSummary(java.lang.String summary) throws android.os.RemoteException;
  /**
     * Let SUW knows the progress is pending now, and stop update progress.
     * @param reasonResName The name of resource identifier.
     * @param quantity The number used to get the correct string for the current language's
     *           plural rules
     * @param formatArgs The format arguments that will be used for substitution.
     */
  public android.os.Bundle setPendingReason(java.lang.String reasonResName, int quantity, int[] formatArgs, int reason) throws android.os.RemoteException;
  /**
     * Once the progress completed, and service can be destroy. Call this function.
     * SUW will unbind the service.
     * @param resName The name of resource identifier.
     * @param quantity The number used to get the correct string for the current language's
     *           plural rules
     * @param formatArgs The format arguments that will be used for substitution.
     */
  public android.os.Bundle setComplete(java.lang.String resName, int quantity, int[] formatArgs) throws android.os.RemoteException;
  /**
     * Once the progress failed, and not able to finish the progress. Should call
     * this function. SUW will unbind service after receive setFailure. Client can
     * registerProgressService again once the service is ready to execute.
     * @param resName The name of resource identifier.
     * @param quantity The number used to get the correct string for the current language's
     *           plural rules
     * @param formatArgs The format arguments that will be used for substitution.
     */
  public android.os.Bundle setFailure(java.lang.String resName, int quantity, int[] formatArgs) throws android.os.RemoteException;
}

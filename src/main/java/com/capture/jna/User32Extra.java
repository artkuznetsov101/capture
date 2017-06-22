package com.capture.jna;

import java.awt.AWTException;
import java.io.IOException;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface User32Extra extends User32 {

	User32Extra INSTANCE = (User32Extra) Native.loadLibrary("user32", User32Extra.class, W32APIOptions.DEFAULT_OPTIONS);

	public HDC GetWindowDC(HWND hWnd);

	public boolean GetClientRect(HWND hWnd, RECT rect);

	public boolean PrintWindow(HWND hWnd, HDC hdcWindow2, int i);

	public boolean IsIconic(HWND hWnd);

	public void GetWindowTextA(HWND hWnd, byte[] buffer, int length);

	public HWND FindWindowEx(HWND hWnd, Object object, String string, Object object2);

	public int GetWindowTextLength(int hWnd);

	boolean EnumWindows(WndEnumProc wndenumproc, int lParam);

	boolean IsWindowVisible(int hWnd);

	int GetWindowRect(int hWnd, RECT r);

	void GetWindowTextW(int hWnd, char[] buffer, int length);

	public HWND SetActiveWindow(HWND hWnd);

	public static interface WndEnumProc extends StdCallLibrary.StdCallCallback {
		boolean callback(int hWnd, int lParam) throws IOException, AWTException;
	}
}

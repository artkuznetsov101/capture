package com.capture.core;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.capture.jna.GDI32Extra;
import com.capture.jna.User32Extra;
import com.capture.jna.WinGDIExtra;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;

public class WindowCapture {

	private static String salt = java.util.UUID.randomUUID().toString();
	private static int i = 0;

	public static void main(String[] args) throws InterruptedException {

		Parameters parameters = new Parameters();
		JCommander jc = JCommander.newBuilder().addObject(parameters).build();
		jc.setProgramName("capture");

		try {
			jc.parse(args);
		} catch (ParameterException ex) {
			System.out.println(ex.getMessage());
			return;
		}

		if (parameters.help) {
			jc.usage();
			return;
		} else {
			System.out.println(parameters);
		}

		System.out.println("Found windows:");
		List<String> captions = WindowCapture.getFullWindowCaption(parameters.caption);
		if (captions.isEmpty()) {
			System.out.println("No matched windows");
			return;
		}

		System.out.println("Capture windows:");
		for (String caption : captions) {
			System.out.println("\t" + caption);
		}

		System.out.println("Capture start...");
		while (!Thread.interrupted()) {
			for (String caption : captions) {

				BufferedImage screen = WindowCapture.capture(caption);
				WindowCapture.save(parameters.path, parameters.extension, screen);
			}
			Thread.sleep(parameters.wait * 1000);
		}
		System.out.println("Capture end...");
	}

	public static BufferedImage capture(String caption) {

		HWND hSrcWnd = User32.INSTANCE.FindWindow(null, caption);
		if (hSrcWnd == null)
			throw new IllegalStateException("Window not found! " + caption);
		// TODO what if no window
		RECT rcSrc = new RECT();
		User32Extra.INSTANCE.GetWindowRect(hSrcWnd, rcSrc);

		HDC hDC1 = User32.INSTANCE.GetDC(hSrcWnd);
		HDC hSrcDC = GDI32.INSTANCE.CreateCompatibleDC(hDC1);
		HBITMAP hBmp = GDI32.INSTANCE.CreateCompatibleBitmap(hDC1, rcSrc.right - rcSrc.left, rcSrc.bottom - rcSrc.top);

		GDI32.INSTANCE.SelectObject(hSrcDC, hBmp);
		User32Extra.INSTANCE.PrintWindow(hSrcWnd, hSrcDC, 0);
		GDI32Extra.INSTANCE.BitBlt(hDC1, 0, 0, rcSrc.right - rcSrc.left, rcSrc.bottom - rcSrc.top, hSrcDC, 0, 0,
				WinGDIExtra.SRCCOPY);

		int width = rcSrc.right - rcSrc.left;
		int height = rcSrc.bottom - rcSrc.top;

		BITMAPINFO bmi = new BITMAPINFO();
		bmi.bmiHeader.biWidth = width;
		bmi.bmiHeader.biHeight = -height;
		bmi.bmiHeader.biPlanes = 1;
		bmi.bmiHeader.biBitCount = 32;
		bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

		Memory buffer = new Memory(width * height * 4);
		GDI32.INSTANCE.GetDIBits(hSrcDC, hBmp, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

		GDI32.INSTANCE.DeleteObject(hBmp);
		GDI32.INSTANCE.DeleteDC(hSrcDC);
		User32.INSTANCE.ReleaseDC(hSrcWnd, hDC1);

		return image;
	}

	public static List<String> getFullWindowCaption(final String shortCaption) {

		final List<String> result = new ArrayList<>();

		User32Extra.INSTANCE.EnumWindows(new User32Extra.WndEnumProc() {

			@Override
			public boolean callback(int hWnd, int lParam) throws IOException, AWTException {

				if (User32Extra.INSTANCE.IsWindowVisible(hWnd)) {

					RECT r = new RECT();
					User32Extra.INSTANCE.GetWindowRect(hWnd, r);
					if (r.left > -32000) { // if minimized
						if (User32Extra.INSTANCE.GetWindowTextLength(hWnd) > 0) {
							char[] buffer = new char[User32Extra.INSTANCE.GetWindowTextLength(hWnd) + 1];
							User32Extra.INSTANCE.GetWindowTextW(hWnd, buffer, buffer.length);
							String title = String.valueOf(buffer).trim();
							// String title = Native.toString(buffer);
							System.out.println("\t" + title);
							if (!title.isEmpty() && title.matches(shortCaption)) {
								result.add(title);
							}

						}
					}
				}
				return true;
			}
		}, 0);
		return result;
	}

	// TODO recur dir creation

	public static void save(String path, String extension, ArrayList<BufferedImage> list) {

		for (BufferedImage image : list) {
			String fileName = salt + "-" + i + "." + extension;
			File f = new File(Paths.get(path).resolve(fileName).toString());
			try {
				ImageIO.write(image, extension, f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	public static void save(String path, String extension, BufferedImage image) {

		String fileName = salt + "-" + i + "." + extension;
		File f = new File(Paths.get(path).resolve(fileName).toString());
		try {
			ImageIO.write(image, extension, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		i++;
	}
}

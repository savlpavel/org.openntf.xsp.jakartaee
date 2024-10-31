/**
 * Copyright (c) 2018-2024 Contributors to the XPages Jakarta EE Support Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.xsp.jakartaee.servlet;

import java.io.IOException;
import java.io.UncheckedIOException;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

class OldServletInputStreamWrapper extends ServletInputStream {
	final javax.servlet.ServletInputStream delegate;

	public OldServletInputStreamWrapper(final javax.servlet.ServletInputStream delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean isFinished() {
		try {
			return delegate.available() == 0;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(final ReadListener readListener) {
		// NOP, hopefully
	}

	@Override
	public int read() throws IOException {
		return delegate.read();
	}

	@Override
	public int read(final byte[] b) throws IOException {
		return delegate.read(b);
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		return delegate.read(b, off, len);
	}

	@Override
	public long skip(final long n) throws IOException {
		return delegate.skip(n);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public int available() throws IOException {
		return delegate.available();
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}

	@Override
	public void mark(final int readlimit) {
		delegate.mark(readlimit);
	}

	@Override
	public void reset() throws IOException {
		delegate.reset();
	}

	@Override
	public boolean markSupported() {
		return delegate.markSupported();
	}

	@Override
	public int readLine(final byte[] b, final int off, final int len) throws IOException {
		return delegate.readLine(b, off, len);
	}

}

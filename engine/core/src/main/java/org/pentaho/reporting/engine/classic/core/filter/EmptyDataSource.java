/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2001 - 2013 Object Refinery Ltd, Hitachi Vantara and Contributors..  All rights reserved.
 */

package org.pentaho.reporting.engine.classic.core.filter;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;

/**
 * A data source that always returns null.
 *
 * @author Thomas Morgner
 */
public final class EmptyDataSource implements DataSource {
  /**
   * Default-Constructor.
   */
  public EmptyDataSource() {
  }

  /**
   * Returns the value for the data source (always null in this case).
   *
   * @param runtime
   *          the expression runtime that is used to evaluate formulas and expressions when computing the value of this
   *          filter.
   * @param element
   * @return always null.
   */
  public Object getValue( final ExpressionRuntime runtime, final ReportElement element ) {
    return null;
  }

  /**
   * Clones the data source.
   *
   * @return a clone.
   * @noinspection CloneDoesntCallSuperClone
   */
  public EmptyDataSource clone() {
    return this;
  }

}

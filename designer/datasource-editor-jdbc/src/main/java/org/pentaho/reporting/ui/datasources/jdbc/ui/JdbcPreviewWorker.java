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
 * Copyright (c) 2009 - 2018 Hitachi Vantara.  All rights reserved.
 */

package org.pentaho.reporting.ui.datasources.jdbc.ui;

import javax.swing.table.TableModel;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ParameterDataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.parameters.AbstractParameter;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterDefinitionEntry;
import org.pentaho.reporting.engine.classic.core.parameters.ReportParameterDefinition;
import org.pentaho.reporting.engine.classic.core.states.LengthLimitingTableModel;
import org.pentaho.reporting.engine.classic.core.states.QueryDataRowWrapper;
import org.pentaho.reporting.engine.classic.core.util.CloseableTableModel;
import org.pentaho.reporting.libraries.designtime.swing.background.CancelEvent;
import org.pentaho.reporting.libraries.designtime.swing.background.PreviewWorker;

public class JdbcPreviewWorker implements PreviewWorker {
  private DataFactory dataFactory;
  private String query;
  private int queryTimeout;
  private int queryLimit;
  private ReportDataFactoryException exception;
  private TableModel tableModel;
  private ReportParameterDefinition reportParameters;

  public JdbcPreviewWorker( final DataFactory dataFactory,
                           final String query,
                           final int queryTimeout,
                           final int queryLimit,
                           final ReportParameterDefinition reportParameters ) {
    this.query = query;
    this.queryTimeout = queryTimeout;
    this.queryLimit = queryLimit;
    this.dataFactory = dataFactory;
    this.reportParameters = reportParameters;
  }

  public ReportDataFactoryException getException() {
    return exception;
  }

  public TableModel getResultTableModel() {
    return tableModel;
  }

  public void close() {
    if ( tableModel instanceof CloseableTableModel ) {
      final CloseableTableModel ct = (CloseableTableModel) tableModel;
      ct.close();
    }
    dataFactory.close();
  }

  /**
   * Convert parameters from ReportParameterDefinition to ParameterDataRow format
   * @param reportParameters
   * @return
   */
  private ParameterDataRow normalizeParameters( ReportParameterDefinition reportParameters ) {
    ParameterDefinitionEntry[] params = reportParameters.getParameterDefinitions();
    String[] namesParams = new String[params.length];
    Object[] objParams = new Object[params.length];

    for ( int i = 0; i < params.length; i++ ) {
      if ( params[i] instanceof AbstractParameter ) {
        AbstractParameter currentParam = (AbstractParameter) params[i];

        namesParams[i] = currentParam.getName();
        objParams[i] = currentParam.getDefaultValue();
      }
    }

    return new ParameterDataRow( namesParams, objParams );
  }

  /**
   * Requests that the thread stop processing as soon as possible.
   */
  public void cancelProcessing( final CancelEvent event ) {
    dataFactory.cancelRunningQuery();
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p/>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see Thread#run()
   */
  public void run() {
    try {
      ParameterDataRow paramDataRow = new ParameterDataRow();
      if ( this.reportParameters != null ) {
        paramDataRow = normalizeParameters( this.reportParameters );
      }

      tableModel = dataFactory.queryData( query, new QueryDataRowWrapper( paramDataRow, queryLimit, queryTimeout ) );

      if ( queryLimit > 0 ) {
        tableModel = new LengthLimitingTableModel( tableModel, queryLimit );
      }
    } catch ( ReportDataFactoryException e ) {
      exception = e;
    }
  }
}

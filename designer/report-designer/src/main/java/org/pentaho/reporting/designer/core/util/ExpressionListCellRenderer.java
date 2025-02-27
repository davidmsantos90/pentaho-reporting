/*!
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
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package org.pentaho.reporting.designer.core.util;

import org.pentaho.reporting.engine.classic.core.metadata.ExpressionMetaData;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class ExpressionListCellRenderer extends DefaultListCellRenderer {
  public ExpressionListCellRenderer() {
  }

  public Component getListCellRendererComponent( final JList list,
                                                 final Object value,
                                                 final int index,
                                                 final boolean isSelected,
                                                 final boolean cellHasFocus ) {
    final JLabel rendererComponent = (JLabel)
      super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
    if ( value instanceof ExpressionMetaData ) {
      final ExpressionMetaData metaData = (ExpressionMetaData) value;

      final boolean deprecated;
      final boolean expert;
      final boolean preferred;
      deprecated = metaData.isDeprecated();
      expert = metaData.isExpert();
      preferred = metaData.isPreferred();

      String prefix = "";
      if ( deprecated ) {
        prefix = "*";
      }
      int fontStyle = Font.PLAIN;
      if ( expert ) {
        fontStyle |= Font.ITALIC;
      }
      if ( preferred ) {
        fontStyle |= Font.BOLD;
      }
      rendererComponent.setFont( getFont().deriveFont( fontStyle ) );
      final String displayName = metaData.getDisplayName( Locale.getDefault() );
      final String groupName = metaData.getGrouping( Locale.getDefault() );
      rendererComponent.setToolTipText( metaData.getDeprecationMessage( Locale.getDefault() ) );
      rendererComponent.setText( prefix + displayName + " (" + groupName + ")" );
    } else {
      rendererComponent.setText( " " );
    }
    return rendererComponent;
  }
}

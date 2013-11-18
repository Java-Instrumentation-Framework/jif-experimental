<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:output method="html"/>
   <xsl:template match="log">
    <html>
      <head>
        <title>
          ExampleLoggingApplication Log
        </title>
      </head>
      <body>
      <h1>ExampleLoggingApplication Log</h1>
          <table border="0">
          <tr>
            <td>
              <h4>Class</h4>
            </td>
            <td width="3"><pre><xsl:text> </xsl:text></pre></td>
            <td nowrap="false">
              <h4>Method</h4>
            </td>
            <td width="5"><pre><xsl:text> </xsl:text></pre></td>
            <td>
              <h4>Message</h4>
            </td>
            <td width="5"><pre><xsl:text> </xsl:text></pre></td>
            <td >
              <h4>Date</h4>
            </td>
            <td width="3"><pre><xsl:text> </xsl:text></pre></td>
            <td>
              <h4>Level</h4>
            </td>
          </tr>
            <xsl:for-each select="record">
              <xsl:apply-templates select="."/>
            </xsl:for-each>
          </table>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="record">
    <tr VALIGN = "top">
      <td>
         <font size="-1">
        <xsl:value-of select="class"/>
          </font> 
      </td>
          <td width="3"><pre><xsl:text> </xsl:text></pre></td>
      <td nowrap="true">
          <font size="-1">
          <xsl:value-of select="method"/><xsl:text>()</xsl:text>
          </font> 
      </td>
          <td width="5"><pre><xsl:text> </xsl:text></pre></td>
      <td><pre>
        <xsl:value-of select="message"/>
        </pre>
      </td>
          <td width="5"><pre><xsl:text> </xsl:text></pre></td>
      <td >
        <xsl:value-of select="date"/>
      </td>
          <td width="3"><pre><xsl:text> </xsl:text></pre></td>
      <td>
        <xsl:value-of select="level"/>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>

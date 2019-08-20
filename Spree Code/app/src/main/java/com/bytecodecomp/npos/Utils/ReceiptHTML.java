package com.bytecodecomp.npos.Utils;

public class ReceiptHTML {

    public static String receipt = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\" name=\"viewport\">\n" +
            "    <title>Receipt</title>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<div>\n" +
            "\n" +
            "    <div id=\"invoice-POS\">\n" +
            "\n" +
            "      <center id=\"top\">\n" +
            "        <div class=\"logo\"></div>\n" +
            "        <div class=\"info\">\n" +
            "          <h2>Spree Store</h2>\n" +
            "        </div><!--End Info-->\n" +
            "      </center><!--End InvoiceTop-->\n" +
            "\n" +
            "      <div id=\"mid\">\n" +
            "        <div class=\"info\">\n" +
            "          <h2>Contact Info</h2>\n" +
            "          <p>\n" +
            "              Address : AddressAddress</br>\n" +
            "              Email   : EmailEmail</br>\n" +
            "              Phone   : PhonePhone</br>\n" +
            "          </p>\n" +
            "        </div>\n" +
            "      </div><!--End Invoice Mid-->\n" +
            "\n" +
            "      <div id=\"bot\">\n" +
            "\n" +
            "  \t\t\t\t\t<div id=\"table\">\n" +
            "  \t\t\t\t\t\t<table>\n" +
            "\n" +
            "\n" +
            "  \t\t\t\t\t\t\t<tr class=\"tabletitle\">\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"item\"><h2>Item</h2></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"Hours\"><h2>Qty</h2></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"Rate\"><h2>S.Total</h2></td>\n" +
            "  \t\t\t\t\t\t\t</tr>\n" +
            "\n" +
            "  \t\t\t\t\t\t\t<tr class=\"service\">\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"tableitem\"><p class=\"itemtext\">1. Product One</p></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"tableitem\"><p class=\"itemtext\">5 Units</p></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"tableitem\"><p class=\"itemtext\">$375.00</p></td>\n" +
            "  \t\t\t\t\t\t\t</tr>\n" +
            "\n" +
            "  \t\t\t\t\t\t\t<tr class=\"tabletitle\">\n" +
            "  \t\t\t\t\t\t\t\t<td></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"Rate\"><h3>tax</h3></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"payment\"><h3>$419.25</h3></td>\n" +
            "  \t\t\t\t\t\t\t</tr>\n" +
            "\n" +
            "  \t\t\t\t\t\t\t<tr class=\"tabletitle\">\n" +
            "  \t\t\t\t\t\t\t\t<td></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"Rate\"><h3>Total</h3></td>\n" +
            "  \t\t\t\t\t\t\t\t<td class=\"payment\"><h3>$3,644.25</h3></td>\n" +
            "  \t\t\t\t\t\t\t</tr>\n" +
            "\n" +
            "  \t\t\t\t\t\t</table>\n" +
            "  \t\t\t\t\t</div><!--End Table-->\n" +
            "\n" +
            "              <center>\n" +
            "                <div id=\"legalcopy\" class=\"info\">\n" +
            "                  <p class=\"legal\"><strong>Thank you for your business!</strong></p>\n" +
            "                </div>\n" +
            "              </center>\n" +
            "\n" +
            "\n" +
            "  \t\t\t\t</div><!--End InvoiceBot-->\n" +
            "    </div><!--End Invoice-->\n" +
            "\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "\n" +
            "</body>\n" +
            "\n" +
            "<style>\n" +
            "\n" +
            "#invoice-POS{\n" +
            "  box-shadow: 0 0 1in -0.25in rgba(0, 0, 0, 0.5);\n" +
            "  padding:2mm;\n" +
            "  margin: 0 auto;\n" +
            "  width: 100mm;\n" +
            "  background: #FFF;\n" +
            "\n" +
            "\n" +
            "::selection {background: #f31544; color: #FFF;}\n" +
            "::moz-selection {background: #f31544; color: #FFF;}\n" +
            "h1{\n" +
            "  font-size: 1.5em;\n" +
            "  color: #222;\n" +
            "}\n" +
            "h2{font-size: .9em;}\n" +
            "h3{\n" +
            "  font-size: 1.2em;\n" +
            "  font-weight: 300;\n" +
            "  line-height: 2em;\n" +
            "}\n" +
            "p{\n" +
            "  font-size: .7em;\n" +
            "  color: #666;\n" +
            "  line-height: 1.2em;\n" +
            "}\n" +
            "\n" +
            "#top, #mid,#bot{ /* Targets all id with 'col-' */\n" +
            "  border-bottom: 1px solid #EEE;\n" +
            "}\n" +
            "\n" +
            "#top{min-height: 100px;}\n" +
            "#mid{min-height: 80px;}\n" +
            "#bot{ min-height: 50px;}\n" +
            "\n" +
            "#top .logo{\n" +
            "  //float: left;\n" +
            "\theight: 60px;\n" +
            "\twidth: 60px;\n" +
            "\tbackground: url(http://michaeltruong.ca/images/logo1.png) no-repeat;\n" +
            "\tbackground-size: 60px 60px;\n" +
            "}\n" +
            ".clientlogo{\n" +
            "  float: left;\n" +
            "\theight: 60px;\n" +
            "\twidth: 60px;\n" +
            "\tbackground: url(http://michaeltruong.ca/images/client.jpg) no-repeat;\n" +
            "\tbackground-size: 60px 60px;\n" +
            "  border-radius: 50px;\n" +
            "}\n" +
            ".info{\n" +
            "  display: block;\n" +
            "  //float:left;\n" +
            "  margin-left: 0;\n" +
            "}\n" +
            ".title{\n" +
            "  float: right;\n" +
            "}\n" +
            ".title p{text-align: right;}\n" +
            "table{\n" +
            "  width: 100%;\n" +
            "  border-collapse: collapse;\n" +
            "}\n" +
            "td{\n" +
            "  //padding: 5px 0 5px 15px;\n" +
            "  //border: 1px solid #EEE\n" +
            "}\n" +
            ".tabletitle{\n" +
            "  //padding: 5px;\n" +
            "  font-size: .5em;\n" +
            "  background: #EEE;\n" +
            "}\n" +
            ".service{border-bottom: 1px solid #EEE;}\n" +
            ".item{width: 24mm;}\n" +
            ".itemtext{font-size: .5em;}\n" +
            "\n" +
            "#legalcopy{\n" +
            "  margin-top: 5mm;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "}\n" +
            "\n" +
            "</style>\n" +
            "\n" +
            "\n" +
            "</html>\n";

}

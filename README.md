CheckBoxText
============

CheckBoxText is a simple view that will allow the text of the CheckBox to be set to the right, left, above or below the CheckBox (there really is not any use for the left or right orientations due to the actual CheckBox and CheckTextView but they are include just in case).
<p>
CheckBoxText view implements Checkable and uses the CompoundButton.OnCheckedChangeListener just like the real CheckBox view. However, within the OnCheckChangeListener the compoundButton.getText() will return an empty string. I have forwarded the actual text showing by the CheckBoxText into the tag of the compoundButton (this is automatically done).
<p>
Some of the CheckBox (setting of the background and isChecked)and TextView (setting the text and the TextAppearance) xml attributes are forwarded when setting the view from xml, there are corresponding methods to everything but the setting of the internal margins of the TextView and heckBox. To set the TextAppearance create a style and any TextView xml attributes can be set.
<p>
TextView xml attributes:
<ul>
<li>textAppearance</li>
<li>text</li>
<li>padding</li>
<li>paddingLeft</li>
<li>paddingRight</li>
<li>paddingTop</li>
<li>paddingBottom</li>
<li>layout_margin(text_margin)</li>
<li>layout_marginLeft(text_marginLeft)</li>
<li>layout_marginRight(text_marginRight)</li>
<li>layout_marginTop (text_marginTop)</li>
<li>layout_marginBottom (text_marginBottom)</li>
</ul>
<p>
CheckBox xml attributes:
<ul>
<li>isChecked</li>
<li>background (checkbox_background)</li>
<li>padding</li>
<li>paddingLeft</li>
<li>paddingRight</li>
<li>paddingTop</li>
<li>paddingBottom</li>
<li>layout_margin(checkbox_margin)</li>
<li>layout_marginLeft(checkbox_marginLeft)</li>
<li>layout_marginRight(checkbox_marginRight)</li>
<li>layout_marginTop (checkbox_marginTop)</li>
<li>layout_marginBottom (checkbox_marginBottom)</li>
</ul>
<p>
Setting the margins will mess up the views but since CheckBox view seems to behave differently with each API level (the default CheckBox drawables have different amounts of padding on them) the margins may need to be adjusted. Also using custom checkbox drawables could mess up the TextView and CheckBox placement.

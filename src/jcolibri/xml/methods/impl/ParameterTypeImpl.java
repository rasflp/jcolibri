//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2004.07.27 at 10:46:52 CEST 
//

package jcolibri.xml.methods.impl;

public class ParameterTypeImpl implements jcolibri.xml.methods.ParameterType,
		com.sun.xml.bind.unmarshaller.UnmarshallableObject,
		com.sun.xml.bind.serializer.XMLSerializable,
		com.sun.xml.bind.validator.ValidatableObject {

	protected java.lang.String _DataType;

	protected java.lang.String _CBRTerm;

	protected java.lang.String _Description;

	protected java.lang.String _Name;

	private final static com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer
			.deserialize("\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/grammar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0003I\u0000\u000ecachedHashCodeL\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0002xp\u0005\u00c6\u0090Eppsq\u0000~\u0000\u0000\u0003\u007f\u008a>ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003\u0001\u00bf\u00c5\u0017pp\u0000sr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003\u0001\u00bf\u00c5\fppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0013L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u0000.com.sun.msv.datatype.xsd.WhiteSpaceProcessor$1\u0013JMoI\u00db\u00a4G\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003\u0000\u0000\u0000\nppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0013L\u0000\fnamespaceURIq\u0000~\u0000\u0013xpq\u0000~\u0000\u0017q\u0000~\u0000\u0016sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0013L\u0000\fnamespaceURIq\u0000~\u0000\u0013xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004Namet\u0000\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001\u0001\u00bf\u00c5\"ppsq\u0000~\u0000\u0007\u0001\u00bf\u00c5\u0017sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000q\u0000~\u0000\u000esq\u0000~\u0000\u001ft\u0000\u000bDescriptionq\u0000~\u0000#sr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003\u0000\u0000\u0000\tsq\u0000~\u0000\'\u0001psq\u0000~\u0000$\u0002G\u0006\u0002ppsq\u0000~\u0000\u0007\u0000`\u0087{pp\u0000sq\u0000~\u0000\u000b\u0000`\u0087pppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u0013xq\u0000~\u0000\u0012q\u0000~\u0000#t\u0000\fDataTypeTypeq\u0000~\u0000\u001a\u0000\u0000q\u0000~\u0000\u0015q\u0000~\u0000\u0015t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0006t\u0000\u0006Stringt\u0000\u0007Integert\u0000\u0006Doublet\u0000\u0007Booleant\u0000\u0004Datet\u0000\u0006Objectxq\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u00008q\u0000~\u0000#sq\u0000~\u0000\u001ft\u0000\bDataTypeq\u0000~\u0000#sq\u0000~\u0000\u0007\u0001\u00e6~\u0085pp\u0000sq\u0000~\u0000\u000b\u0001\u00e6~zppsr\u0000\'com.sun.msv.datatype.xsd.FinalComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalValuexr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypeq\u0000~\u00005xq\u0000~\u0000\u0012q\u0000~\u0000#t\u0000\u000bCBRTermTypeq\u0000~\u0000\u001aq\u0000~\u0000\u0015\u0000\u0000\u0000\u0000q\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u0000\u0017q\u0000~\u0000#sq\u0000~\u0000\u001ft\u0000\u0007CBRTermq\u0000~\u0000#sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0002\u0000\u0004I\u0000\u0005countI\u0000\tthresholdL\u0000\u0006parentq\u0000~\u0000O[\u0000\u0005tablet\u0000![Lcom/sun/msv/grammar/Expression;xp\u0000\u0000\u0000\u0004\u0000\u0000\u00009pur\u0000![Lcom.sun.msv.grammar.Expression;\u00d68D\u00c3]\u00ad\u00a7\n\u0002\u0000\u0000xp\u0000\u0000\u0000\u00bfpppppppppq\u0000~\u0000%ppq\u0000~\u0000\u0006ppppppppppppppppppppppppppppppppppppq\u0000~\u0000.ppppppppppppppppq\u0000~\u0000\u0005pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

	private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
		return jcolibri.xml.methods.ParameterType.class;
	}

	public java.lang.String getDataType() {
		return _DataType;
	}

	public void setDataType(java.lang.String value) {
		_DataType = value;
	}

	public java.lang.String getCBRTerm() {
		return _CBRTerm;
	}

	public void setCBRTerm(java.lang.String value) {
		_CBRTerm = value;
	}

	public java.lang.String getDescription() {
		return _Description;
	}

	public void setDescription(java.lang.String value) {
		_Description = value;
	}

	public java.lang.String getName() {
		return _Name;
	}

	public void setName(java.lang.String value) {
		_Name = value;
	}

	public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(
			com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
		return new jcolibri.xml.methods.impl.ParameterTypeImpl.Unmarshaller(
				context);
	}

	public java.lang.Class getPrimaryInterfaceClass() {
		return PRIMARY_INTERFACE_CLASS();
	}

	public void serializeElements(
			com.sun.xml.bind.serializer.XMLSerializer context)
			throws org.xml.sax.SAXException {
		context.startElement("", "Name");
		context.endAttributes();
		try {
			context.text(((java.lang.String) _Name));
		} catch (java.lang.Exception e) {
			com.sun.xml.bind.marshaller.Util.handlePrintConversionException(
					this, e, context);
		}
		context.endElement();
		if (_Description != null) {
			context.startElement("", "Description");
			context.endAttributes();
			try {
				context.text(((java.lang.String) _Description));
			} catch (java.lang.Exception e) {
				com.sun.xml.bind.marshaller.Util
						.handlePrintConversionException(this, e, context);
			}
			context.endElement();
		}
		if ((_CBRTerm == null) && (_DataType != null)) {
			context.startElement("", "DataType");
			context.endAttributes();
			try {
				context.text(((java.lang.String) _DataType));
			} catch (java.lang.Exception e) {
				com.sun.xml.bind.marshaller.Util
						.handlePrintConversionException(this, e, context);
			}
			context.endElement();
		} else {
			if ((_CBRTerm != null) && (_DataType == null)) {
				context.startElement("", "CBRTerm");
				context.endAttributes();
				try {
					context.text(((java.lang.String) _CBRTerm));
				} catch (java.lang.Exception e) {
					com.sun.xml.bind.marshaller.Util
							.handlePrintConversionException(this, e, context);
				}
				context.endElement();
			}
		}
	}

	public void serializeAttributes(
			com.sun.xml.bind.serializer.XMLSerializer context)
			throws org.xml.sax.SAXException {
	}

	public void serializeAttributeBodies(
			com.sun.xml.bind.serializer.XMLSerializer context)
			throws org.xml.sax.SAXException {
	}

	public java.lang.Class getPrimaryInterface() {
		return (jcolibri.xml.methods.ParameterType.class);
	}

	public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
		return new com.sun.msv.verifier.regexp.REDocumentDeclaration(
				schemaFragment);
	}

	public class Unmarshaller extends
			com.sun.xml.bind.unmarshaller.ContentHandlerEx {

		public Unmarshaller(
				com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
			super(context, "-----------");
		}

		protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
			return jcolibri.xml.methods.impl.ParameterTypeImpl.this;
		}

		public void enterElement(java.lang.String ___uri,
				java.lang.String ___local, org.xml.sax.Attributes __atts)
				throws com.sun.xml.bind.unmarshaller.UnreportedException {
			switch (state) {
			case 6:
				revertToParentFromEnterElement(___uri, ___local, __atts);
				return;
			case 0:
				if (("" == ___uri) && ("Name" == ___local)) {
					context.pushAttributes(__atts);
					state = 1;
					return;
				}
				break;
			case 3:
				if (("" == ___uri) && ("CBRTerm" == ___local)) {
					context.pushAttributes(__atts);
					state = 9;
					return;
				}
				if (("" == ___uri) && ("Description" == ___local)) {
					context.pushAttributes(__atts);
					state = 7;
					return;
				}
				if (("" == ___uri) && ("DataType" == ___local)) {
					context.pushAttributes(__atts);
					state = 4;
					return;
				}
				break;
			}
			super.enterElement(___uri, ___local, __atts);
		}

		public void leaveElement(java.lang.String ___uri,
				java.lang.String ___local)
				throws com.sun.xml.bind.unmarshaller.UnreportedException {
			switch (state) {
			case 6:
				revertToParentFromLeaveElement(___uri, ___local);
				return;
			case 2:
				if (("" == ___uri) && ("Name" == ___local)) {
					context.popAttributes();
					state = 3;
					return;
				}
				break;
			case 8:
				if (("" == ___uri) && ("Description" == ___local)) {
					context.popAttributes();
					state = 3;
					return;
				}
				break;
			case 5:
				if (("" == ___uri) && ("DataType" == ___local)) {
					context.popAttributes();
					state = 6;
					return;
				}
				break;
			case 10:
				if (("" == ___uri) && ("CBRTerm" == ___local)) {
					context.popAttributes();
					state = 6;
					return;
				}
				break;
			}
			super.leaveElement(___uri, ___local);
		}

		public void enterAttribute(java.lang.String ___uri,
				java.lang.String ___local)
				throws com.sun.xml.bind.unmarshaller.UnreportedException {
			switch (state) {
			case 6:
				revertToParentFromEnterAttribute(___uri, ___local);
				return;
			}
			super.enterAttribute(___uri, ___local);
		}

		public void leaveAttribute(java.lang.String ___uri,
				java.lang.String ___local)
				throws com.sun.xml.bind.unmarshaller.UnreportedException {
			switch (state) {
			case 6:
				revertToParentFromLeaveAttribute(___uri, ___local);
				return;
			}
			super.leaveAttribute(___uri, ___local);
		}

		public void text(java.lang.String value)
				throws com.sun.xml.bind.unmarshaller.UnreportedException {
			try {
				switch (state) {
				case 6:
					revertToParentFromText(value);
					return;
				case 4:
					try {
						_DataType = value;
					} catch (java.lang.Exception e) {
						handleParseConversionException(e);
					}
					state = 5;
					return;
				case 9:
					try {
						_CBRTerm = value;
					} catch (java.lang.Exception e) {
						handleParseConversionException(e);
					}
					state = 10;
					return;
				case 7:
					try {
						_Description = value;
					} catch (java.lang.Exception e) {
						handleParseConversionException(e);
					}
					state = 8;
					return;
				case 1:
					try {
						_Name = value;
					} catch (java.lang.Exception e) {
						handleParseConversionException(e);
					}
					state = 2;
					return;
				}
			} catch (java.lang.RuntimeException e) {
				handleUnexpectedTextException(value, e);
			}
		}

	}

}
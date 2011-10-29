/*
 * Copyright (c) 2011, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eel.kitchen.jsonschema.v2.keyword;

import eel.kitchen.jsonschema.v2.schema.SchemaFactory;
import eel.kitchen.jsonschema.v2.schema.ValidationMode;
import eel.kitchen.jsonschema.v2.schema.ValidationState;
import eel.kitchen.util.NodeType;
import org.codehaus.jackson.JsonNode;

import java.util.EnumSet;

import static eel.kitchen.jsonschema.v2.schema.ValidationMode.*;

public final class DisallowKeywordValidator
    extends TypesetKeywordValidator
{
    public DisallowKeywordValidator(final JsonNode schema)
    {
        super("disallow", schema);
    }

    @Override
    public void validate(final ValidationState state, final JsonNode node)
    {
        final NodeType nodeType = NodeType.getNodeType(node);

        if (typeSet.contains(nodeType)) {
            state.addMessage("instance matches forbidden type " + nodeType);
            return;
        }

        if (nextSchemas.isEmpty())
            return;

        buildNext(state.getFactory());

        state.setNextSchema(nextSchema);
    }

    @Override
    protected void buildNext(final SchemaFactory factory)
    {
        final EnumSet<ValidationMode> mode
            = EnumSet.of(VALIDATE_NEGATE, VALIDATE_ANY);
        nextSchema = factory.buildSchema(mode, nextSchemas);
    }
}

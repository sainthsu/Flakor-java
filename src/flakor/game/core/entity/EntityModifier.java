/*
 * EntityModifier.java
 * Created on 8/26/13 8:04 PM
 *
 * ver0.0.1beta 8/26/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.core.entity;

import flakor.game.core.modifier.BaseModifier;

/**
 * Created by saint on 8/26/13.
 */
public abstract class EntityModifier extends BaseModifier<EntityInterface> implements EntityModifierInterface
{
    // ===========================================================
    // Constructors
    // ===========================================================

    public EntityModifier() {
        super();
    }

    public EntityModifier(final EntityModifierListener pEntityModifierListener)
    {
        super(pEntityModifierListener);
    }

}
